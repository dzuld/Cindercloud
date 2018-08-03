package cloud.cinder.vechain.importer.block.continuous;

import cloud.cinder.vechain.importer.transaction.TransactionReceiptProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

public class Tryout {

    @Autowired
    private TransactionReceiptProcessor transactionReceiptProcessor;

    @PostConstruct
    public void init() {
        transactionReceiptProcessor.importTransactionReceipt("0xcd1a4cf9224bb14017d9a262deca2b81f592a1d0f6152d275ec578b38ae841fa");
    }
}
