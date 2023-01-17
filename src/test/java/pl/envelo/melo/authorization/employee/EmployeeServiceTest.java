package pl.envelo.melo.authorization.employee;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import pl.envelo.melo.authorization.person.Person;
import pl.envelo.melo.authorization.user.User;
import pl.envelo.melo.domain.event.Event;
import pl.envelo.melo.domain.event.EventRepository;
import pl.envelo.melo.domain.event.EventType;
import pl.envelo.melo.domain.event.dto.EventToDisplayOnListDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class EmployeeServiceTest {
    @Autowired
    EmployeeService employeeService;
    @Autowired
    EmployeeRepository employeeRepository;
    @Autowired
    EventRepository eventRepository;

    @Test
    @Transactional
    void getSetOfOwnedEvents() {
        //Testy
        ResponseEntity<?> responseEntity = employeeService.getSetOfOwnedEvents(1);
        assertEquals(HttpStatus.OK, employeeService.getSetOfOwnedEvents(1).getStatusCode());
        Set<EventToDisplayOnListDto> events = (Set<EventToDisplayOnListDto>) responseEntity.getBody();
        assertEquals(1,events.size());//Test name
        assertEquals("Test name", events.stream().findFirst().get().getName());
        assertEquals(HttpStatus.valueOf(404), employeeService.getSetOfOwnedEvents(2).getStatusCode());


    }
}