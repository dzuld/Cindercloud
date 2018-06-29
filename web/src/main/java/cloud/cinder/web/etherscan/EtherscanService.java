package cloud.cinder.web.etherscan;

import cloud.cinder.web.block.service.BlockService;
import cloud.cinder.web.etherscan.dto.EtherscanResponse;
import cloud.cinder.web.etherscan.dto.EtherscanTransaction;
import cloud.cinder.common.login.domain.LoginEvent;
import cloud.cinder.web.transaction.service.TransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import static cloud.cinder.ethereum.util.EthUtil.prettifyAddress;

@Service
@Slf4j
public class EtherscanService {

    private final EtherscanClient etherscanClient;
    private final BlockService blockService;
    private final TransactionService transactionService;

    public EtherscanService(final EtherscanClient etherscanClient, final BlockService blockService, final TransactionService transactionService) {
        this.etherscanClient = etherscanClient;
        this.blockService = blockService;
        this.transactionService = transactionService;
    }

    @Value("${cloud.cinder.etherscan-enabled:false}")
    private boolean enabled;

    @Async
    public void importByAddress(final String address) {
        if (enabled) {
            try {
                final EtherscanResponse transactions = etherscanClient.transactions(address);
                if (transactions.isSuccess() && transactions.getResult() != null) {
                    transactions.getResult()
                            .stream()
                            .map(EtherscanTransaction::getBlockHash)
                            .filter(blockHash -> !transactionService.findByBlock(prettifyAddress(blockHash), new PageRequest(0, 1)).hasContent())
                            .forEach(x -> {
                                try {
                                    log.trace("importing block {}", x);
                                    blockService.getBlock(x).subscribe();
                                } catch (final Exception ex) {
                                    log.debug("unable to find block {}", x);
                                }
                            });
                }
            } catch (final Exception ex) {
                log.debug("Unable to fetch transactions from etherscan for address {}", address);
            }
        }
    }

    @Async
    @EventListener
    public void etherscanAfterImport(final LoginEvent loginEvent) {
        importByAddress(loginEvent.getWallet());
    }
}
