package cloud.cinder.web.block.service;

import cloud.cinder.ethereum.block.domain.Block;
import cloud.cinder.web.block.repository.BlockRepository;
import cloud.cinder.common.queue.QueueSender;
import cloud.cinder.ethereum.web3j.Web3jGateway;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.EthGetBlockTransactionCountByHash;
import rx.Observable;

import java.math.BigInteger;
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

    @Transactional(propagation = Propagation.REQUIRES_NEW)
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

    private void propagateTransactions(final Block savedBlock) {
        log.trace("Propagating Transactions");
        web3j.web3j().ethGetBlockTransactionCountByHash(savedBlock.getHash())
                .observable()
                .filter(Objects::nonNull)
                .map(EthGetBlockTransactionCountByHash::getTransactionCount)
                .filter(Objects::nonNull)
                .filter(txCount -> txCount.compareTo(BigInteger.ZERO) != 0)
                .subscribe(txCount -> {
                    try {
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
                .forEach(this::save);
    }

    private boolean wasWronglySavedAsNormalBlock(final String hash, final boolean uncle) {
        return uncle && blockRepository.findBlock(hash).isPresent();
    }

    @Transactional
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
}
