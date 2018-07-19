package cloud.cinder.web.login.repository;

import cloud.cinder.common.infrastructure.repository.JpaRepository;
import cloud.cinder.common.login.domain.LoginEvent;

public interface LoginEventRepository extends JpaRepository<LoginEvent, Long> {
}
