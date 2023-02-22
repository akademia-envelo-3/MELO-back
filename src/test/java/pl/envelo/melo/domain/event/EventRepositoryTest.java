package pl.envelo.melo.domain.event;

import pl.envelo.melo.EventContextTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


class EventRepositoryTest extends EventContextTest {
    SimpleEventMocker simpleEventMocker;

    //@Test
    void findAllByStartTimeGreaterThan() {
        Event presentEvent = simpleEventMocker.mockEvent(LocalDateTime.now().plusDays(5), EventType.UNLIMITED_PUBLIC_INTERNAL);
        Event pastEvent = simpleEventMocker.mockEvent(LocalDateTime.now().minusDays(5), EventType.UNLIMITED_PUBLIC_INTERNAL);
        assertEquals(1, eventRepository.findAllByStartTimeAfterAndType(LocalDateTime.now(), EventType.UNLIMITED_PUBLIC_INTERNAL).size());
        assertTrue(eventRepository.findAllByStartTimeAfterAndType(LocalDateTime.now(), EventType.UNLIMITED_PUBLIC_INTERNAL).get(0).getStartTime().compareTo(LocalDateTime.now()) > 0);
    }
}