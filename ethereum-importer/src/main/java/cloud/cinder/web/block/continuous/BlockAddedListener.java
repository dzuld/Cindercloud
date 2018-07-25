package cloud.cinder.web.block.continuous;

import cloud.cinder.ethereum.block.domain.Block;
import cloud.cinder.web.transaction.continuous.TransactionImporter;
import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@ConditionalOnProperty(name = "cloud.cinder.ethereum.queue-block-added-transaction-import", havingValue = "true")
public class BlockAddedListener {

    @Autowired
    private TransactionImporter transactionImporter;
    @Autowired
    private ObjectMapper objectMapper;
    private final Meter blockImportMeter;


    public BlockAddedListener(final MetricRegistry metricRegistry) {
        this.blockImportMeter = metricRegistry.meter("transactions_of_block_importer");
    }

    public void receiveMessage(final String blockAsString) {
        try {
            log.debug("Fetched from queue: {}", blockAsString);
            final Block convertedBlock = objectMapper.readValue(blockAsString, Block.class);
            transactionImporter.importTransactions(convertedBlock);
        } catch (final Exception ex) {
            log.error("Error trying to receive message", ex);
            throw new IllegalArgumentException("Unable to process");
        } finally {
            blockImportMeter.mark();
        }
    }
}