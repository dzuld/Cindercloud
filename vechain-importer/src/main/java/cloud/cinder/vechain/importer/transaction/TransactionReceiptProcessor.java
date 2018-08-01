package cloud.cinder.vechain.importer.transaction;

import cloud.cinder.vechain.importer.transaction.service.VechainTransactionService;
import cloud.cinder.vechain.thorifyj.ThorifyjGateway;
import cloud.cinder.vechain.thorifyj.transaction.domain.ThorifyTransactionReceipt;
import cloud.cinder.vechain.transaction.VechainTransactionEvent;
import cloud.cinder.vechain.transaction.VechainTransactionReceipt;
import cloud.cinder.vechain.transaction.VechainTransactionReceiptOutput;
import cloud.cinder.vechain.transaction.VechainTransactionTransfer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TransactionReceiptProcessor {

    @Autowired
    private ThorifyjGateway gateway;
    @Autowired
    private VechainTransactionService vechainTransactionService;

    @Async
    public void importTransactionReceipt(final String id) {
        try {
            if (!vechainTransactionService.receiptImported(id)) {
                final ThorifyTransactionReceipt rawReceipt = gateway.thorify().transactions().getReceipt(id);
                final VechainTransactionReceipt receipt = VechainTransactionReceipt
                        .of(id, rawReceipt);
                final VechainTransactionReceipt savedReceipt = vechainTransactionService.save(receipt);

                rawReceipt.getOutputs()
                        .forEach(x -> {
                            VechainTransactionReceiptOutput output = VechainTransactionReceiptOutput.of(x, savedReceipt);
                            VechainTransactionReceiptOutput savedOutput = vechainTransactionService.save(output);

                            x.getTransfers()
                                    .forEach(thorifyTransfer -> {
                                        VechainTransactionTransfer transfer = VechainTransactionTransfer.of(thorifyTransfer, savedOutput);
                                        vechainTransactionService.save(transfer);
                                    });

                            x.getEvents()
                                    .forEach(thorifyEvent -> {
                                        VechainTransactionEvent event = VechainTransactionEvent.of(thorifyEvent, savedOutput);
                                        vechainTransactionService.save(event);
                                    });
                        });
            } else {
                log.debug("{} was already imported", id);
            }
        } catch (final Exception excepion) {
            log.error("Unable to process", excepion);
        }
    }
}
