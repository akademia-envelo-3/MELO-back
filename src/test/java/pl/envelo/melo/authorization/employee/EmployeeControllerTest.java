package pl.envelo.melo.authorization.employee;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import pl.envelo.melo.domain.event.EventRepository;
import pl.envelo.melo.domain.event.dto.EventToDisplayOnListDto;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class EmployeeControllerTest {

    @Autowired
    EmployeeController employeeController;
    @Autowired
    EmployeeRepository employeeRepository;
    @Autowired
    EventRepository eventRepository;
    @Test
    @Transactional
    void getOwnedEvents() {
        //Testy
        ResponseEntity<?> responseEntity = employeeController.getOwnedEvents(1);
        assertEquals(HttpStatus.OK, employeeController.getOwnedEvents(1).getStatusCode());
        Set<EventToDisplayOnListDto> events = (Set<EventToDisplayOnListDto>) responseEntity.getBody();
        assertEquals(1,events.size());//Test name
        assertEquals("Test name", events.stream().findFirst().get().getName());
        assertEquals(HttpStatus.valueOf(404), employeeController.getOwnedEvents(2).getStatusCode());


    }
}