package pl.envelo.melo.authorization.employee;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import pl.envelo.melo.EventContextTest;
import pl.envelo.melo.domain.event.Event;
import pl.envelo.melo.domain.event.EventType;
import pl.envelo.melo.domain.event.dto.EventToDisplayOnListDto;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EmployeeControllerTest extends EventContextTest {

    @Autowired
    EmployeeController employeeController;

    @Test
    @Transactional
    void getOwnedEvents() {
        //Testy
        Event event = simpleEventMocker.mockEvent(LocalDateTime.now().plusDays(5), EventType.LIMITED_PUBLIC_INTERNAL);
        event.setName("Test name");
        event.getOrganizer().setOwnedEvents(new HashSet<>());
        event.getOrganizer().setJoinedEvents(new HashSet<>());
        event.getOrganizer().getOwnedEvents().add(event);
        event.getOrganizer().getJoinedEvents().add(event);
        ResponseEntity<?> responseEntity = employeeController.getOwnedEvents(1);
        assertEquals(HttpStatus.OK, employeeController.getOwnedEvents(1).getStatusCode());
        Set<EventToDisplayOnListDto> events = (Set<EventToDisplayOnListDto>) responseEntity.getBody();
        assertEquals(1, events.size());//Test name
        assertEquals("Test name", events.stream().findFirst().get().getName());
        assertEquals(HttpStatus.valueOf(404), employeeController.getOwnedEvents(2).getStatusCode());
    }
}