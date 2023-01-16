package pl.envelo.melo.domain.event;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.envelo.melo.authorization.employee.Employee;
import pl.envelo.melo.authorization.employee.EmployeeRepository;
import pl.envelo.melo.authorization.person.Person;
import pl.envelo.melo.authorization.person.PersonRepository;
import pl.envelo.melo.authorization.user.UserRepository;
import pl.envelo.melo.domain.event.dto.EventToDisplayOnListDto;
import pl.envelo.melo.domain.event.dto.NewEventDto;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class EventServiceTest {
    @Autowired
    EventService eventService;
    @Autowired
    EventRepository eventRepository;
    @Autowired
    EmployeeRepository employeeRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    PersonRepository personRepository;

    SimpleEventMocker simpleEventMocker;

    @BeforeEach
    void setUp() {
        eventRepository.deleteAll();
        simpleEventMocker = new SimpleEventMocker(employeeRepository, eventRepository, personRepository, userRepository);
    }

    @Test
    void listAllEvents() {
        Event presentEvent = simpleEventMocker.mockEvent(LocalDateTime.now().plusDays(5), EventType.LIMITED_PUBLIC_INTERNAL, simpleEventMocker.mockEmployee("test"), simpleEventMocker.mockEmployee("test2"));
        Event presentPrivateEvent = simpleEventMocker.mockEvent(LocalDateTime.now().plusDays(5), EventType.LIMITED_PRIVATE_INTERNAL, simpleEventMocker.mockEmployee("test"), simpleEventMocker.mockEmployee("test2"));
        Event pastEvent = simpleEventMocker.mockEvent(LocalDateTime.now().minusDays(5),EventType.UNLIMITED_PUBLIC_INTERNAL);
        EventToDisplayOnListDto eventToDisplayOnListDto = eventService.listAllEvents().getBody().get(0);
        assertEquals(2, eventToDisplayOnListDto.getInvitedMembersNumber());
        assertEquals(presentEvent.getId(), eventToDisplayOnListDto.getEventId());
        assertEquals(1, eventService.listAllEvents().getBody().size());
        assertNull(eventToDisplayOnListDto.getMainPhoto());
    }
    @Test
    void updateEvent() {
        Event event = simpleEventMocker.mockEvent(LocalDateTime.now().plusDays(5), EventType.LIMITED_PUBLIC_INTERNAL);
        eventRepository.save(event);
        Employee employee = simpleEventMocker.mockEmployee("owwneer");
        NewEventDto newEventDto = new NewEventDto();
        newEventDto.setEventType(EventType.LIMITED_PUBLIC_INTERNAL);
        newEventDto.setOrganizerId(employee.getId());
        newEventDto.setName("Ddds");
        newEventDto.setDescription("dsadsa");
        newEventDto.setPeriodicType(PeriodicType.NONE);
        eventService.updateEvent(event.getId(), newEventDto);
        event = eventRepository.getReferenceById(event.getId());
        System.out.println(event.getOrganizer().getUser().getPerson().getFirstName());
        System.out.println(event.getMembers().stream().filter(e-> employee.getUser().getPerson().getId() == e.getId()).findFirst().get().getFirstName());
        //eventService.updateEvent(event.getId(), );
    }
}