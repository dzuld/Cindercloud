package cloud.cinder.vechain.thorifyj;

import cloud.cinder.vechain.thorifyj.block.service.ThorifyBlockService;
import cloud.cinder.vechain.thorifyj.transaction.service.ThorifyTransactionService;
import org.springframework.stereotype.Component;

@Component
public class Thorifyj {
    private ThorifyBlockService blockService;
    private ThorifyTransactionService transactionService;

    public Thorifyj(final ThorifyBlockService blockService, final ThorifyTransactionService transactionService) {
        this.blockService = blockService;
        this.transactionService = transactionService;
    }

    public ThorifyBlockService blocks() {
        return blockService;
    }

    public ThorifyTransactionService transactions() {
        return transactionService;
    }
}
