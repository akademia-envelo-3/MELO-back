package pl.envelo.melo.domain.event;

import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class EventCreator {

    private final EventRepository eventRepository;
    private final EventService eventService;

    public EventCreator(EventRepository eventRepository, EventService eventService) {
        this.eventRepository = eventRepository;
        this.eventService = eventService;
    }

    @Transactional
    @Scheduled(fixedDelayString = "${melo.fixed-delay}", timeUnit = TimeUnit.SECONDS) //TODO change to 12h
    public void createEvent() {
        LocalDateTime currentTime = LocalDateTime.now();
        List<Event> cycleEventList = eventRepository.findByPeriodicTypeNotAndNextEventIsNullAndUnitIsNotNull(PeriodicType.NONE);
        for (Event event : cycleEventList) {
            if (event.getStartTime().compareTo(currentTime) < 0) {
                eventService.createEventFromExisting(event, event.getStartTime());
            }
        }
    }
}
