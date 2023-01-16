package pl.envelo.melo.domain.event;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pl.envelo.melo.authorization.employee.Employee;
import pl.envelo.melo.authorization.employee.EmployeeRepository;
import pl.envelo.melo.authorization.person.PersonRepository;
import pl.envelo.melo.authorization.user.UserRepository;
import pl.envelo.melo.domain.event.dto.EventDetailsDto;
import pl.envelo.melo.mappers.EventDetailsMapper;

import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class EventServiceTest {

    @Autowired
    EventDetailsMapper eventDetailsMapper;
    @Autowired
    EventService eventService;
    @Autowired
    EventRepository eventRepository;
    @Autowired
    EmployeeRepository employeeRepository;

    @Test
    @Transactional
    void getExistEvent() {

        Event event = eventRepository.findById(1).get();
        EventDetailsDto eventDetailsDto = eventDetailsMapper.convert(event);
        ResponseEntity<?> eventDetailsDtoResponseEntity = eventService.getEvent(1);

        assertEquals(HttpStatus.OK, eventDetailsDtoResponseEntity.getStatusCode());
        assertEquals(event.getName(),eventDetailsDto.getName());
        assertEquals(event.getDescription(),eventDetailsDto.getDescription());
        assertEquals(event.getOrganizer().getUser().getPerson().getFirstName()
                ,eventDetailsDto.getOrganizer().getFirstName());
        assertEquals(event.getCategory().getName()
                ,eventDetailsDto.getCategory());
        assertEquals(event.getMemberLimit(),eventDetailsDto.getMemberLimit());

        Optional<Employee> organizer = employeeRepository.findById(event.getOrganizer().getId());
        assertEquals(organizer.get().getUser().getPerson().getFirstName(),eventDetailsDto.getOrganizer().getFirstName());
    }

    @Test
    @Transactional
    void checkNonExistentEvent(){
        ResponseEntity<?> eventDetailsDtoResponseEntity = eventService.getEvent(3);
        assertEquals(HttpStatus.NOT_FOUND, eventDetailsDtoResponseEntity.getStatusCode());
    }
}