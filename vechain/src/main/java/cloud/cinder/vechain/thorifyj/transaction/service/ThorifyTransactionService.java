package cloud.cinder.vechain.thorifyj.transaction.service;

import cloud.cinder.vechain.thorifyj.transaction.domain.ThorifyTransaction;
import cloud.cinder.vechain.thorifyj.transaction.domain.ThorifyTransactionReceipt;
import cloud.cinder.vechain.thorifyj.transaction.repository.ThorifyTransactionRepository;
import org.springframework.stereotype.Component;

@Component
public class ThorifyTransactionService {
    private ThorifyTransactionRepository thorifyTransactionRepository;

    public ThorifyTransactionService(final ThorifyTransactionRepository thorifyTransactionRepository) {
        this.thorifyTransactionRepository = thorifyTransactionRepository;
    }

    public ThorifyTransaction get(final String id) {
        return thorifyTransactionRepository.get(id);
    }

    public ThorifyTransactionReceipt getReceipt(final String id) {
        return thorifyTransactionRepository.getReceipt(id);
    }
}
