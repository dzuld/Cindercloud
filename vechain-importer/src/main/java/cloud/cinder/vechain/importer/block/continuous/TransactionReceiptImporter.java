package cloud.cinder.vechain.importer.block.continuous;

import cloud.cinder.vechain.thorifyj.ThorifyjGateway;
import cloud.cinder.vechain.thorifyj.transaction.domain.ThorifyTransactionReceipt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class TransactionReceiptImporter {

    @Autowired
    private ThorifyjGateway gateway;

    @PostConstruct
    public void init() {
        ThorifyTransactionReceipt receipt = gateway.thorify().transactions().getReceipt("0x14710b8bf0116cfcfb07d63b89dccd364f965d56f66f3161ae4336b1c575064f");
        System.out.println(receipt);
    }
}
