package cloud.cinder.web.block.service;

import cloud.cinder.common.queue.QueueSender;
import cloud.cinder.ethereum.block.domain.Block;
import cloud.cinder.web.block.repository.BlockRepository;
import cloud.cinder.ethereum.web3j.Web3jGateway;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.EthBlockNumber;
import org.web3j.protocol.core.methods.response.EthGetBlockTransactionCountByHash;
import rx.Observable;

import java.math.BigInteger;
import java.util.List;
import java.util.Objects;

@Component
@Slf4j
public class BlockService {

    @Autowired
    private Web3jGateway web3j;
    @Autowired
    private BlockRepository blockRepository;
    @Autowired
    private QueueSender $;
    @Autowired
    private ObjectMapper objectMapper;

    @Value("${cloud.cinder.queue.block-added}")
    private String blockQueue;

    @Transactional
    public Block save(final Block block) {
        final Block savedBlock = blockRepository.save(block);
        try {
            propagateTransactions(savedBlock);
        } catch (final Exception ex) {
            log.error("Error while trying to get transactions from block", ex);
        }
        importUncles(block);
        return savedBlock;
    }

    @Transactional(readOnly = true)
    public Observable<Slice<Block>> findByMiner(final String address, final Pageable pageable) {
        return Observable.just(blockRepository.findBlocksAndUnclesByMiner(address, pageable));
    }

    private void propagateTransactions(final Block savedBlock) {
        web3j.web3j().ethGetBlockTransactionCountByHash(savedBlock.getHash())
                .observable()
                .filter(Objects::nonNull)
                .map(EthGetBlockTransactionCountByHash::getTransactionCount)
                .filter(Objects::nonNull)
                .filter(txCount -> txCount.compareTo(BigInteger.ZERO) != 0)
                .subscribe(txCount -> {
                    try {
                        log.trace("Propagating Transactions for block {}", savedBlock.getHeight());
                        $.send(blockQueue, objectMapper.writeValueAsString(savedBlock));
                    } catch (final Exception ex) {
                        log.error("Problem while trying to send block with transactions to queue", ex);
                    }
                });
    }

    private void importUncles(final Block block) {
        web3j.web3j().ethGetBlockByHash(block.getHash(), false)
                .observable()
                .filter(Objects::nonNull)
                .map(EthBlock::getBlock)
                .filter(Objects::nonNull)
                .flatMapIterable(EthBlock.Block::getUncles)
                .forEach(x -> importByHash(x, true));
    }

    @Transactional
    public void importByHash(final String hash, final boolean uncle) {
        log.trace("importing block or uncle by hash");
        if (wasWronglySavedAsNormalBlock(hash, uncle)) {
            blockRepository.delete(hash);
        }

        web3j.web3j().ethGetBlockByHash(hash, false)
                .observable()
                .map(EthBlock::getBlock)
                .filter(Objects::nonNull)
                .map(block -> {
                    if (uncle) {
                        return Block.asUncle(block);
                    } else {
                        return Block.asBlock(block);
                    }
                })
                .forEach(newBlock -> {
                    if (blockRepository.exists(newBlock.getHash())) {
                        blockRepository.delete(newBlock.getHash());
                        blockRepository.save(newBlock);
                    }
                });
    }

    private boolean wasWronglySavedAsNormalBlock(final String hash, final boolean uncle) {
        return uncle && blockRepository.findBlock(hash).isPresent();
    }

    public Observable<Block> getUncle(final String hash) {
        return blockRepository.findUncle(hash)
                .map(Observable::just).orElse(Observable.empty());
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Observable<Block> getBlock(final String hash) {
        return blockRepository.findBlock(hash)
                .map(Observable::just)
                .orElseGet(() ->
                        {
                            log.debug("Block {} was not found in repository, fetching from web3.", hash);
                            return web3j.web3j().ethGetBlockByHash(hash, false)
                                    .observable()
                                    .map(EthBlock::getBlock)
                                    .filter(Objects::nonNull)
                                    .map(Block::asBlock)
                                    .map(this::save);
                        }
                );
    }

    @Transactional(readOnly = true)
    public List<Block> getLast10IndexedBlocks() {
        return blockRepository.findAllBlocksOrderByHeightDescAsList(new PageRequest(0, 10));
    }

    @Transactional(readOnly = true)
    public Slice<Block> getLastBlocks(final Pageable pageable) {
        return blockRepository.findAllBlocksOrderByHeightDescAsPage(pageable);
    }

    @Transactional(readOnly = true)
    public Slice<Block> getLastUncles(final Pageable pageable) {
        return blockRepository.findAllUnclesOrderByHeightDesc(pageable);
    }

    @Transactional(readOnly = true)
    public Slice<Block> searchBlocks(final String searchKey, final String minedBy, final Pageable pageable) {
        return blockRepository.searchBlocks(searchKey, minedBy, pageable);
    }

    @Transactional(readOnly = true)
    public Slice<Block> searchUncles(final String searchKey, final Pageable pageable) {
        return blockRepository.searchUncles(searchKey, pageable);
    }

    public Observable<EthBlockNumber> getLastBlock() {
        return web3j.web3j().ethBlockNumber().observable();
    }
}
