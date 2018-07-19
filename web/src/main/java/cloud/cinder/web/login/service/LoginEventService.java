package cloud.cinder.web.login.service;

import cloud.cinder.web.login.repository.LoginEventRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LoginEventService {

    private LoginEventRepository loginEventRepository;

    public LoginEventService(final LoginEventRepository loginEventRepository) {
        this.loginEventRepository = loginEventRepository;
    }

    @Transactional(readOnly = true)
    public Long totalLogins() {
        return loginEventRepository.count();
    }
}
