package cloud.cinder.vechain.importer.block.continuous;

import cloud.cinder.common.queue.QueueSender;
import cloud.cinder.vechain.block.domain.VechainBlock;
import cloud.cinder.vechain.importer.block.service.VechainBlockService;
import cloud.cinder.vechain.thorifyj.ThorifyjGateway;
import cloud.cinder.vechain.thorifyj.block.domain.ThorifyBlock;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Optional;

@Component
@Slf4j
@EnableScheduling
@EnableAsync
public class BlockImporter {

    private ThorifyjGateway thorifyjGateway;
    private VechainBlockService vechainBlockService;
    private QueueSender queueSender;
    private ObjectMapper objectMapper;
    private HistoricBlockImporter historicBlockImporter;

    @Value("${cloud.cinder.queue.vechain-block-added}")
    private String blockAddedQueue;

    @Value("${cloud.cinder.vechain.live-block-import:false}")
    private boolean autoBlockImport;

    public BlockImporter(final ThorifyjGateway thorifyjGateway,
                         final VechainBlockService vechainBlockService,
                         final QueueSender queueSender,
                         final ObjectMapper objectMapper,
                         final HistoricBlockImporter historicBlockImporter) {
        this.thorifyjGateway = thorifyjGateway;
        this.vechainBlockService = vechainBlockService;
        this.queueSender = queueSender;
        this.objectMapper = objectMapper;
        this.historicBlockImporter = historicBlockImporter;
    }



    @PostConstruct
    public void init() {
        historicBlockImporter.importHistoricBlocks();
    }

   // @Scheduled(fixedRate = 1000)
    public void listenForBlocks() {
        if (autoBlockImport) {
            importNewestBlock();
        }
    }

    private void importNewestBlock() {
        Optional<ThorifyBlock> newBlock = getLatest();
        newBlock
                .filter(block -> !vechainBlockService.exists(block.getId()))
                .ifPresent(block -> {
                    vechainBlockService.save(VechainBlock.asVechainBlock(block));
                    if (block.getTransactions() != null && !block.getTransactions().isEmpty()) {
                        try {
                            queueSender.send(blockAddedQueue, objectMapper.writeValueAsString(block));
                        } catch (final Exception ex) {
                            log.error("Unable to send newly found vechan block to queue");
                        }
                    }
                });
    }

    private Optional<ThorifyBlock> getLatest() {
        try {
            return Optional.ofNullable(thorifyjGateway.thorify().blocks().getLatest());
        } catch (final Exception ex) {
            log.debug("Unable to get latest block: {}", ex.getMessage());
            return Optional.empty();
        }
    }
}
