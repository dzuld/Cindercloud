package cloud.cinder.web.block.continuous;

import cloud.cinder.ethereum.block.domain.BlockImportJob;
import cloud.cinder.web.block.continuous.repository.BlockImportJobRepository;
import cloud.cinder.ethereum.block.domain.Block;
import cloud.cinder.web.block.repository.BlockRepository;
import cloud.cinder.web.block.service.BlockService;
import cloud.cinder.ethereum.web3j.Web3jGateway;
import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.web3j.protocol.core.DefaultBlockParameterNumber;
import org.web3j.protocol.core.methods.response.EthBlock;
import rx.Subscription;

import java.util.Date;
import java.util.Objects;

@Component
@Slf4j
@EnableScheduling
public class BlockImporter {

    private final Meter historicBlockImportMeter;
    @Autowired
    private Web3jGateway web3j;
    @Autowired
    private BlockService blockService;
    @Autowired
    private BlockRepository blockRepository;
    @Autowired
    private BlockImportJobRepository blockImportJobRepository;

    @Value("${cloud.cinder.ethereum.live-block-import:false}")
    private boolean autoBlockImport;


    private Subscription liveSubscription;


    @Autowired
    public BlockImporter(final MetricRegistry metricRegistry) {
        this.historicBlockImportMeter = metricRegistry.meter("historic_block_importer");
    }

    @Scheduled(fixedRate = 60000)
    public void listenToBlocks() {
        if (autoBlockImport) {
            if (this.liveSubscription == null) {
                this.liveSubscription = subscribe();
            } else {
                final Subscription newSubscription = subscribe();
                this.liveSubscription.unsubscribe();
                this.liveSubscription = newSubscription;
            }
        }
    }

    private Subscription subscribe() {
        return web3j.web3j().blockObservable(false)
                .map(EthBlock::getBlock)
                .filter(Objects::nonNull)
                .map(Block::asBlock)
                .subscribe(block -> {
                    try {
                        log.trace("received live block");
                        blockService.save(block);
                    } catch (final Exception exc) {
                        log.error("unable to save block {}", block.getHeight());
                        log.error("error: ", exc);
                    }
                }, onError -> {
                    log.debug("Error while looking for new blocks", onError);
                    this.liveSubscription.unsubscribe();
                    this.liveSubscription = subscribe();
                });
    }

    void execute(final BlockImportJob job) {
        log.debug("Starting job {}", job.getId());
        job.setActive(true);
        job.setStartTime(new Date());
        blockImportJobRepository.save(job);

        for (long i = job.getFromBlock(); i <= job.getToBlock(); i++) {
            if (i % 25 == 0) {
                log.debug("Historic Import: trying to import block: {}", i);
            }
            try {
                final EthBlock ethBlock = web3j.web3j().ethGetBlockByNumber(new DefaultBlockParameterNumber(i), false)
                        .observable().toBlocking().firstOrDefault(null);
                if (ethBlock != null && ethBlock.getBlock() != null && !blockRepository.findOne(ethBlock.getBlock().getHash()).isPresent()) {
                    try {
                        blockService.save(Block.asBlock(ethBlock.getBlock()));
                    } catch (final Exception exc) {
                        log.debug("unable to save block", exc);
                    }
                } else {
                    log.trace("couldn't find {} in web3 or already imported", i);
                }
            } catch (final Exception exc) {
                log.debug("unable to get block");
            } finally {
                historicBlockImportMeter.mark();
            }
        }

        job.setActive(false);
        job.setEndTime(new Date());
        blockImportJobRepository.save(job);
    }
}