package pl.envelo.melo.domain.event;

import jakarta.transaction.Transactional;

import org.junit.Ignore;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
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
import pl.envelo.melo.domain.event.dto.EventToDisplayOnListDto;

import java.util.Objects;
import java.util.Optional;
import java.time.LocalDateTime;

@Transactional
@SpringBootTest
class EventServiceTest {

    @Autowired
    UserRepository userRepository;
    @Autowired
    PersonRepository personRepository;
    @Autowired
    EventDetailsMapper eventDetailsMapper;
    @Autowired
    EventService eventService;
    @Autowired
    EventRepository eventRepository;
    @Autowired
    EmployeeRepository employeeRepository;
    SimpleEventMocker simpleEventMocker;
    
    void setUpRepo() {
        simpleEventMocker = new SimpleEventMocker(employeeRepository, eventRepository, personRepository, userRepository);
    }
    
    @Test
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
    void checkNonExistentEvent(){
        ResponseEntity<?> eventDetailsDtoResponseEntity = eventService.getEvent(3);
        assertEquals(HttpStatus.NOT_FOUND, eventDetailsDtoResponseEntity.getStatusCode());
        }


    void listAllEvents() {
        setUpRepo();
        Event presentEvent = simpleEventMocker.mockEvent(LocalDateTime.now().plusDays(5), EventType.LIMITED_PUBLIC_INTERNAL, simpleEventMocker.mockEmployee("test"), simpleEventMocker.mockEmployee("test2"));
        Event presentPrivateEvent = simpleEventMocker.mockEvent(LocalDateTime.now().plusDays(5), EventType.LIMITED_PRIVATE_INTERNAL, simpleEventMocker.mockEmployee("test"), simpleEventMocker.mockEmployee("test2"));
        Event pastEvent = simpleEventMocker.mockEvent(LocalDateTime.now().minusDays(5),EventType.UNLIMITED_PUBLIC_INTERNAL);
        EventToDisplayOnListDto eventToDisplayOnListDto = eventService.listAllEvents().getBody().get(0);
        assertEquals(2, eventToDisplayOnListDto.getInvitedMembersNumber());
        assertEquals(presentEvent.getId(), eventToDisplayOnListDto.getEventId());
        assertEquals(1, eventService.listAllEvents().getBody().size());
        assertNull(eventToDisplayOnListDto.getMainPhoto());
    }
}