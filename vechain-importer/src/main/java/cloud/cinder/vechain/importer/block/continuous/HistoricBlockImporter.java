package cloud.cinder.vechain.importer.block.continuous;

import cloud.cinder.common.queue.QueueSender;
import cloud.cinder.vechain.block.domain.VechainBlock;
import cloud.cinder.vechain.importer.block.service.VechainBlockService;
import cloud.cinder.vechain.thorifyj.ThorifyjGateway;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.Objects;
import java.util.stream.LongStream;

@Component
@Slf4j
public class HistoricBlockImporter {

    private ThorifyjGateway thorifyjGateway;
    private VechainBlockService vechainBlockService;
    private QueueSender queueSender;
    private ObjectMapper objectMapper;


    @Value("${cloud.cinder.queue.vechain-block-added}")
    private String blockAddedQueue;


    public HistoricBlockImporter(final ThorifyjGateway thorifyjGateway, final VechainBlockService vechainBlockService, final QueueSender queueSender, final ObjectMapper objectMapper) {
        this.thorifyjGateway = thorifyjGateway;
        this.vechainBlockService = vechainBlockService;
        this.queueSender = queueSender;
        this.objectMapper = objectMapper;
    }

    @Value("${cloud.cinder.vechain.historic-block-import:false}")
    private boolean historicBlockImport;

    @Async
    public void importHistoricBlocks() {
        if (historicBlockImport) {
            BigInteger number = thorifyjGateway.thorify().blocks().getLatest().getNumber();
            LongStream.rangeClosed(0, number.longValue())
                    .mapToObj(x -> {
                        try {
                            return thorifyjGateway.thorify().blocks().get(x);
                        } catch (final Exception ex) {
                            log.debug("Unable to get vechain block {}", number);
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .filter(x -> !vechainBlockService.exists(x.getId()))
                    .forEach(x -> {
                        vechainBlockService.save(VechainBlock.asVechainBlock(x));
                        if (x.getTransactions() != null && !x.getTransactions().isEmpty()) {
                            try {
                                queueSender.send(blockAddedQueue, objectMapper.writeValueAsString(x));
                            } catch (final Exception ex) {
                                log.error("Unable to send historic vechain block to queue");
                            }
                        }
                    });
        }
    }
}
