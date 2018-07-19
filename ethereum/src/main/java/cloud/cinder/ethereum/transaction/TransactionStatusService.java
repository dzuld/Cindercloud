package cloud.cinder.ethereum.transaction;

import cloud.cinder.ethereum.transaction.domain.TransactionStatus;
import cloud.cinder.ethereum.web3j.Web3jGateway;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.web3j.protocol.core.methods.response.EthGetTransactionReceipt;

@Component
@Slf4j
public class TransactionStatusService {

    private final Web3jGateway web3jGateway;

    public TransactionStatusService(final Web3jGateway web3jGateway) {
        this.web3jGateway = web3jGateway;
    }

    public TransactionStatus getTransactionStatus(final org.web3j.protocol.core.methods.response.Transaction tx) {
        try {
            final EthGetTransactionReceipt send = web3jGateway.web3j().ethGetTransactionReceipt(tx.getHash()).send();

            if (send.getTransactionReceipt().isPresent()) {
                if (send.getTransactionReceipt().get().getStatus().equalsIgnoreCase("1") || send.getTransactionReceipt().get().getStatus().equalsIgnoreCase("0x1")) {
                    return TransactionStatus.SUCCESS;
                } else {
                    if (send.getTransactionReceipt().get().getGasUsed().equals(tx.getGas())) {
                        return TransactionStatus.THROWN;
                    } else {
                        return TransactionStatus.REVERTED;
                    }
                }
            } else {
                return TransactionStatus.UNKNOWN;
            }
        } catch (final Exception ex) {
            log.debug("Unable to fetch transaction receipt");
            return TransactionStatus.UNKNOWN;
        }
    }

}
