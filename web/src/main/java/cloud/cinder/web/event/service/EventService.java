package cloud.cinder.web.event.service;

import cloud.cinder.common.event.domain.Event;
import cloud.cinder.common.event.domain.EventType;
import cloud.cinder.web.event.repository.EventRepository;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EventService {

    private EventRepository eventRepository;

    public EventService(final EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @EventListener
    @Transactional
    public void saveEvent(final Event event) {
        eventRepository.save(event);
    }

    @Transactional(readOnly = true)
    public Long countByType(final EventType eventType) {
        return eventRepository.countByType(eventType);
    }
}
