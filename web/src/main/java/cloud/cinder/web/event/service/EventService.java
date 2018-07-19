package cloud.cinder.web.event.service;

import cloud.cinder.common.event.domain.Event;
import cloud.cinder.common.event.domain.EventType;
import cloud.cinder.web.event.repository.EventRepository;
import cloud.cinder.web.notifications.Noty;
import cloud.cinder.common.event.domain.Event;
import cloud.cinder.common.event.domain.EventType;
import cloud.cinder.web.event.repository.EventRepository;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EventService {

    private final EventRepository eventRepository;
    private final Noty noty;

    public EventService(final EventRepository eventRepository, final Noty noty) {
        this.eventRepository = eventRepository;
        this.noty = noty;
    }

    @EventListener
    @Transactional
    public void saveEvent(final Event event) {
        eventRepository.save(event);
        noty.success("Someone just created a new wallet!");
    }

    @Transactional(readOnly = true)
    public Long countByType(final EventType eventType) {
        return eventRepository.countByType(eventType);
    }
}
