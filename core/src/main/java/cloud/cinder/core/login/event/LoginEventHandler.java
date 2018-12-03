package cloud.cinder.core.login.event;

import cloud.cinder.common.login.domain.LoginEvent;
import cloud.cinder.core.login.repository.LoginEventRepository;
import cloud.cinder.core.transaction.service.TransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Slf4j
public class LoginEventHandler {

    private final LoginEventRepository loginEventRepository;
    private final TransactionService transactionService;

    public LoginEventHandler(final LoginEventRepository loginEventRepository,
                             final TransactionService transactionService) {
        this.loginEventRepository = loginEventRepository;
        this.transactionService = transactionService;
    }

    @EventListener
    @Transactional
    @Async
    public void loginOccurred(final LoginEvent loginEvent) {
        try {
            transactionService.indexFromEtherscan(loginEvent.getWallet());
        } catch (final Exception ex) {
            log.error(ex.getMessage());
            log.error("Unable to import from etherscan for wallet {}", loginEvent.getWallet());
        }
        loginEventRepository.save(loginEvent);
    }
}