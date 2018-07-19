package cloud.cinder.web.event.repository;

import cloud.cinder.common.event.domain.Event;
import cloud.cinder.common.event.domain.EventType;
import cloud.cinder.common.infrastructure.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface EventRepository extends JpaRepository<Event, Long> {

    long countByType(final @Param("type") EventType type);
}
