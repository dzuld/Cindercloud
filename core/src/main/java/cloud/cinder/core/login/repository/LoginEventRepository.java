package cloud.cinder.core.login.repository;

import cloud.cinder.common.login.domain.LoginEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoginEventRepository extends JpaRepository<LoginEvent, Long> {
}
