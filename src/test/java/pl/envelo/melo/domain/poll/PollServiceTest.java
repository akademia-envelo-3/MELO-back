package pl.envelo.melo.domain.poll;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.envelo.melo.authorization.employee.EmployeeRepository;
import pl.envelo.melo.authorization.person.PersonRepository;
import pl.envelo.melo.authorization.user.UserRepository;
import pl.envelo.melo.domain.event.EventRepository;
import pl.envelo.melo.domain.event.SimpleEventGenerator;

@Transactional
@SpringBootTest
class PollServiceTest {
    @Autowired
    PollService pollService;
    @Autowired
    EmployeeRepository employeeRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    PersonRepository personRepository;
    @Autowired
    EventRepository eventRepository;
    SimpleEventGenerator simpleEventGenerator;

    @BeforeEach
    void setUp() {
        simpleEventGenerator = new SimpleEventGenerator(employeeRepository, eventRepository, personRepository, userRepository);
    }

    //@Test
    void calculatePollResults() {
    }

//    @Test
//    void insertNewPollTemplate() {
//        Event event = simpleEventGenerator.mockEvent(LocalDateTime.now(), EventType.UNLIMITED_EXTERNAL);
//        PollTemplateDto pollTemplateDto = new PollTemplateDto();
//        pollTemplateDto.setPollOptions(new HashSet<>());
//        pollTemplateDto.setPollQuestion("question");
//        pollTemplateDto.getPollOptions().add("answer a");
//        assertEquals(ResponseEntity.badRequest().body("Poll must have minimum of 2 options and maximum of 10 options"), pollService.insertNewPollTemplate(pollTemplateDto, event.getId()));
//        pollTemplateDto.getPollOptions().add("answer b");
//        pollTemplateDto.getPollOptions().add("answer c");
//        pollTemplateDto.getPollOptions().add("answer d");
//        assertEquals(ResponseEntity.created(URI.create(String.format("events/%d/polls/%d",event.getId(),1))).build(),pollService.insertNewPollTemplate(pollTemplateDto, event.getId()));
//        assertNotNull(event.getPolls());
//        assertTrue(event.getPolls().stream().findFirst().isPresent());
//        assertTrue(event.getPolls().stream().findFirst().get().getPollTemplate().getPollOptions().contains("answer b"));
//    }

    //@Test
    void getPollTemplate() {
    }

    //@Test
    void listAllPollsForEvent() {
    }

    //@Test
    void insertNewPollAnswer() {
    }
}