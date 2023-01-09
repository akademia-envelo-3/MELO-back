package pl.envelo.melo.domain.event;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.envelo.melo.authorization.employee.Employee;
import pl.envelo.melo.authorization.person.Person;
import pl.envelo.melo.domain.comment.Comment;
import pl.envelo.melo.domain.event.dto.EventDetailsDto;
import pl.envelo.melo.domain.event.dto.EventToDisplayOnListDto;
import pl.envelo.melo.domain.event.dto.NewEventDto;
import pl.envelo.melo.domain.poll.Poll;
import pl.envelo.melo.domain.poll.PollAnswer;
import pl.envelo.melo.domain.poll.PollTemplate;

import java.util.List;

@RestController
@AllArgsConstructor
public class EventController {

    private final EventService eventService;
    private final EmployeeService employeeService;
    private final HashtagService hashtagService;
    private final CategoryService categoryService;
    private final AttachmentService attachmentService;
    private final LocationService locationService;
    private final PollTemplateService pollTemplateService;
    private final PollService pollService;
    private final PollAnswerService pollAnswerService;
    private final CommentService commentService;
    private final PersonService personService;

    @GetMapping()
    public ResponseEntity<List<EventToDisplayOnListDto>> getEvents() {
        return null;
    }

    @GetMapping()
    public  ResponseEntity<EventDetailsDto> getEvent(int id) {
        return null;
    }

    @GetMapping()
    public  ResponseEntity<List<Person>> getEventMembers() {
        return null;
    }

    @GetMapping()
    public  ResponseEntity<EmployeeNameDto> getEventOrganizer(int id) {
        return null;
    }

    @PostMapping()
    public boolean changeEventOrganizer(int id, Employee employee) {
        return false;
    }

    @PostMapping()
    public  ResponseEntity<Event> addEvent(NewEventDto newEventDto) {  //void?
        return null;
    }

    @PostMapping()
    public  ResponseEntity<Comment> addCommentToEvent(CommentDto commentDto) {
        return null;
    }

    @PostMapping()
    public  ResponseEntity<Poll> addPollToEvent(PollTemplateDto pollTemplateDto) {
        return null;
    }

    @PostMapping()
    public  ResponseEntity<PollAnswer> addPollAnswer(PollAnswerDto pollAnswerDto) {
        return null;
    }

    @PostMapping()
    public ResponseEntity<?> addGuestToEvent(AddGuestToEventDto addGuestToEventDto) {
        return null;
    }

    @PostMapping()
    public ResponseEntity<?> removeGuestFromEvent(int personId, int eventId) {
        return null;
    }

    @PostMapping()
    public ResponseEntity<?> joinEvent(int employeeId, int eventId) {
        return null;
    }

    @PostMapping()
    public ResponseEntity<?> acceptInvite(int employeeId, int eventId) {
        return null;
    }

    @PostMapping()
    public ResponseEntity<?> filterEvent(String stringFilter) {
        return null;
    }

    @GetMapping
    public  ResponseEntity<List<Person>> getAllInvited(int eventId){
        return null;
    }

    @PostMapping()
    public ResponseEntity<?> disjoinEvent(int employeeId, int eventId){
        return null;
    }
}
