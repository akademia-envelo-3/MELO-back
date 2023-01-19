package pl.envelo.melo.domain.event;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import pl.envelo.melo.authorization.employee.EmployeeRepository;
import pl.envelo.melo.authorization.person.PersonRepository;
import pl.envelo.melo.authorization.user.UserRepository;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
@Transactional
@SpringBootTest
class EventRepositoryTest {
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PersonRepository personRepository;
    SimpleEventMocker simpleEventMocker;
    void setUpRepo() {
        simpleEventMocker = new SimpleEventMocker(employeeRepository, eventRepository, personRepository, userRepository);
    }

    @AfterEach
    void tearDown() {
    }

    void findAllByStartTimeGreaterThan() {
        setUpRepo();
        Event presentEvent = simpleEventMocker.mockEvent(LocalDateTime.now().plusDays(5), EventType.UNLIMITED_PUBLIC_INTERNAL);
        Event pastEvent = simpleEventMocker.mockEvent(LocalDateTime.now().minusDays(5), EventType.UNLIMITED_PUBLIC_INTERNAL);
        assertEquals(1, eventRepository.findAllByStartTimeAfterAndType(LocalDateTime.now(), EventType.UNLIMITED_PUBLIC_INTERNAL).size());
        assertTrue(eventRepository.findAllByStartTimeAfterAndType(LocalDateTime.now(), EventType.UNLIMITED_PUBLIC_INTERNAL).get(0).getStartTime().compareTo(LocalDateTime.now()) > 0);
    }
}