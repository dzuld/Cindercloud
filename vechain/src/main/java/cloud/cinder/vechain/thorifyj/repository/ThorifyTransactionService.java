package cloud.cinder.vechain.thorifyj.repository;

import cloud.cinder.vechain.thorifyj.domain.ThorifyTransaction;
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
}
