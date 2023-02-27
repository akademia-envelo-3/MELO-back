package pl.envelo.melo.domain.event;

import pl.envelo.melo.EventContextTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


class EventRepositoryTest extends EventContextTest {
    SimpleEventGenerator simpleEventGenerator;

    //@Test
    void findAllByStartTimeGreaterThan() {
        Event presentEvent = simpleEventGenerator.mockEvent(LocalDateTime.now().plusDays(5), EventType.UNLIMITED_PUBLIC_INTERNAL);
        Event pastEvent = simpleEventGenerator.mockEvent(LocalDateTime.now().minusDays(5), EventType.UNLIMITED_PUBLIC_INTERNAL);
        assertEquals(1, eventRepository.findAllByStartTimeAfterAndType(LocalDateTime.now(), EventType.UNLIMITED_PUBLIC_INTERNAL).size());
        assertTrue(eventRepository.findAllByStartTimeAfterAndType(LocalDateTime.now(), EventType.UNLIMITED_PUBLIC_INTERNAL).get(0).getStartTime().compareTo(LocalDateTime.now()) > 0);
    }
}