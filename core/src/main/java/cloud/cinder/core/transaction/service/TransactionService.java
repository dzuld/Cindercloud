package cloud.cinder.core.transaction.service;

import cloud.cinder.core.address.service.AddressService;
import cloud.cinder.core.ethereum.block.methods.MethodSignatureService;
import cloud.cinder.core.ethereum.block.service.BlockService;
import cloud.cinder.core.etherscan.dto.EtherscanResponse;
import cloud.cinder.core.token.service.TokenService;
import cloud.cinder.core.transaction.repository.TransactionRepository;
import cloud.cinder.ethereum.address.domain.SpecialAddress;
import cloud.cinder.ethereum.block.domain.Block;
import cloud.cinder.ethereum.parity.domain.MethodSignature;
import cloud.cinder.ethereum.transaction.TransactionStatusService;
import cloud.cinder.ethereum.transaction.domain.Transaction;
import cloud.cinder.ethereum.web3j.Web3jGateway;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.reactivex.Flowable;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import rx.Observable;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class TransactionService {

    @Autowired
    private Web3jGateway web3jGateway;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private BlockService blockService;
    @Autowired
    private AddressService addressService;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private MethodSignatureService methodSignatureService;
    @Autowired
    private TransactionStatusService transactionStatusService;
    private OkHttpClient httpClient;
    private ObjectMapper objectMapper;

    @PostConstruct
    public void init() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(5, TimeUnit.MINUTES);
        builder.writeTimeout(5, TimeUnit.MINUTES);
        builder.readTimeout(5, TimeUnit.MINUTES);
        httpClient = builder.build();
        objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Transactional
    public Flowable<Slice<Transaction>> findByAddress(final String address, final Pageable pageable) {
        final Slice<Transaction> result = transactionRepository.findByAddressFromOrTo(address, pageable);
        result.getContent().forEach(this::enrichWithSpecialAddresses);
        return Flowable.just(result);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Transaction save(final Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    @Transactional(readOnly = true)
    public Flowable<Slice<Transaction>> getTransactionsForBlock(final String blockHash, final Pageable pageable) {
        final Slice<Transaction> result = transactionRepository.findAllByBlockHashOrderByBlockHeightDesc(blockHash, pageable);
        result.getContent().forEach(this::enrichWithSpecialAddresses);
        return Flowable.just(result);
    }

    @Transactional
    public Flowable<Transaction> getTransaction(final String transactionHash) {
        return transactionRepository.findById(transactionHash)
                .map(Flowable::just)
                .orElse(getInternalTransaction(transactionHash)
                        .map(this::enrichWithSpecialAddresses));
    }

    @Transactional(readOnly = true)
    public Optional<MethodSignature> getMethodSignature(final String transactionHash) {
        return getTransaction(transactionHash)
                .map(x -> {
                    if (x.hasInput()) {
                        return methodSignatureService.findSignature(x.getInput());
                    } else {
                        return Optional.<MethodSignature>empty();
                    }
                }).blockingFirst(Optional.empty());
    }

    public Transaction enrichWithSpecialAddresses(final Transaction tx) {
        Optional<SpecialAddress> byAddress = addressService.findByAddress(tx.getFromAddress());
        if (byAddress.isPresent()) {
            tx.setSpecialFrom(byAddress.get());
        } else {
            tokenService.findByAddress(tx.getFromAddress()).ifPresent(x -> {
                tx.setSpecialFrom(
                        SpecialAddress.builder()
                                .address(tx.getFromAddress())
                                .name(x.getName())
                                .slug(x.getSlug())
                                .url(x.getWebsite())
                                .build()
                );
            });
        }
        addressService.findByAddress(tx.getFromAddress()).ifPresent(tx::setSpecialFrom);

        final String to = tx.isContractCreation() ? tx.getCreates() : tx.getToAddress();
        final Optional<SpecialAddress> specialToAddress = addressService.findByAddress(to);
        if (specialToAddress.isPresent()) {
            tx.setSpecialTo(specialToAddress.get());
        } else {
            tokenService.findByAddress(to).ifPresent(x -> {
                tx.setSpecialTo(
                        SpecialAddress.builder()
                                .address(to)
                                .name(x.getName())
                                .slug(x.getSlug())
                                .url(x.getWebsite())
                                .build()
                );
            });
        }
        return tx;
    }

    @Transactional(readOnly = true)
    public Slice<Transaction> getLastTransactions(Pageable pageable) {
        return transactionRepository.findAllOrOrderByBlockTimestampAsPage(pageable);
    }

    @Transactional(readOnly = true)
    public List<Transaction> getLast10Transactions() {
        return transactionRepository.findAllOrOrderByBlockTimestampAsList(new PageRequest(0, 10));
    }

    private Flowable<Transaction> getInternalTransaction(final String transactionHash) {
        try {
            return web3jGateway.web3j().ethGetTransactionByHash(transactionHash)
                    .flowable()
                    .filter(x -> x.getTransaction().isPresent())
                    .map(transaction -> transaction.getTransaction().get())
                    .map(tx -> {
                        final Block block = blockService.getBlock(tx.getBlockHash()).blockingFirst();
                        if (block != null) {
                            return Transaction.builder()
                                    .blockHash(tx.getBlockHash())
                                    .blockHeight(block.getHeight())
                                    .fromAddress(tx.getFrom())
                                    .gas(tx.getGas())
                                    .hash(tx.getHash())
                                    .input(tx.getInput())
                                    .toAddress(tx.getTo())
                                    .blockTimestamp(Date.from(LocalDateTime.ofEpochSecond(block.getTimestamp().longValue(), 0, ZoneOffset.UTC).atOffset(ZoneOffset.UTC).toInstant()))
                                    .value(tx.getValue())
                                    .gasPrice(tx.getGasPrice())
                                    .creates(tx.getCreates())
                                    .s(tx.getS())
                                    .r(tx.getR())
                                    .v(tx.getV())
                                    .status(transactionStatusService.getTransactionStatus(tx))
                                    .nonce(tx.getNonce())
                                    .transactionIndex(tx.getTransactionIndex())
                                    .build();
                        } else {
                            log.error("couldnt import {} because we dont have the block yet", tx.getHash());
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .map(tx -> transactionRepository.save(tx));
        } catch (
                Exception ex) {
            return Flowable.error(ex);
        }
    }

    @Transactional
    public void indexFromEtherscan(final String address) {
        try {
            final Call call = httpClient.newCall(new Request.Builder().url(String.format("http://api.etherscan.io/api?module=account&action=txlist&address=%s&startblock=0&endblock=99999999&sort=asc&apikey=HPK1JKX6586DVIIYGGAPRPNAD9I9Z3Q8PS", address)).build());
            Response execute = call.execute();
            String string = execute.body().string();
            EtherscanResponse transactions = objectMapper.readValue(string, EtherscanResponse.class);

            transactions.getResult().stream()
                    .map(x -> x.getBlockHash())
                    .forEach(x -> blockService.getBlock(x));
        } catch (final Exception ex) {
            log.error(ex.getMessage());
            ex.printStackTrace();
            log.error("Unable to import from etherscan");
        }
    }


    public Slice<Transaction> findByBlock(final String block, final Pageable pageable) {
        return transactionRepository.findByBlockHash(block, pageable);
    }

    public Slice<Transaction> findByBlockAndAddress(final String block, final String address, final Pageable pageable) {
        return transactionRepository.findByAddressFromOrToAndBlockHash(address, block, pageable);
    }

    @Transactional
    public Transaction reindex(final String txId) {
        log.debug("reindexing " + txId);
        transactionRepository.findById(txId)
                .ifPresent(x -> transactionRepository.delete(x));
        return getInternalTransaction(txId).blockingFirst();
    }
}
