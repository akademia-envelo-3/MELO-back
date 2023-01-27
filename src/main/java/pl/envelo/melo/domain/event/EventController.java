package pl.envelo.melo.domain.event;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.transaction.Transactional;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.envelo.melo.authorization.employee.Employee;
import pl.envelo.melo.authorization.employee.EmployeeService;
import pl.envelo.melo.authorization.employee.dto.EmployeeNameDto;
import pl.envelo.melo.authorization.person.Person;
import pl.envelo.melo.authorization.person.PersonService;
import pl.envelo.melo.authorization.person.dto.AddGuestToEventDto;
import pl.envelo.melo.domain.attachment.AttachmentService;
import pl.envelo.melo.domain.category.CategoryService;
import pl.envelo.melo.domain.comment.Comment;
import pl.envelo.melo.domain.comment.CommentService;
import pl.envelo.melo.domain.comment.dto.CommentDto;
import pl.envelo.melo.domain.event.dto.EventDetailsDto;
import pl.envelo.melo.domain.event.dto.EventToDisplayOnListDto;
import pl.envelo.melo.domain.event.dto.NewEventDto;
import pl.envelo.melo.domain.hashtag.HashtagService;
import pl.envelo.melo.domain.location.LocationService;
import pl.envelo.melo.domain.poll.Poll;
import pl.envelo.melo.domain.poll.PollAnswer;
import pl.envelo.melo.domain.poll.PollService;
import pl.envelo.melo.domain.poll.dto.PollAnswerDto;
import pl.envelo.melo.domain.poll.dto.PollTemplateDto;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/v1/event")
public class EventController {

    private final EventService eventService;
    private final EmployeeService employeeService;
    private final HashtagService hashtagService;
    private final CategoryService categoryService;
    private final AttachmentService attachmentService;
    private final LocationService locationService;
    private final PollService pollService;
    private final CommentService commentService;
    private final PersonService personService;


    @GetMapping()
    public ResponseEntity<List<EventToDisplayOnListDto>> getEvents() {
        return eventService.listAllEvents();
    }


    @PostMapping("/{id}")
    public ResponseEntity<?> editEvent(@RequestParam("id") int id, @RequestBody NewEventDto newEventDto) {
        return eventService.updateEvent(id, newEventDto);
    }
    @GetMapping("/{id}/edit-form")
    public ResponseEntity<?> editForm(@RequestParam("id") int id){
        return eventService.editEventForm(id);
    }
    //    @GetMapping()

    @GetMapping("/{id}")
    public ResponseEntity<EventDetailsDto> getEvent(@RequestParam("id") int id) {
        return (ResponseEntity<EventDetailsDto>) eventService.getEvent(id);
    }

    //    @GetMapping()
    public ResponseEntity<List<Person>> getEventMembers() {
        return null;
    }

    //    @GetMapping()
    public ResponseEntity<EmployeeNameDto> getEventOrganizer(int id) {
        return null;
    }

    //    @PostMapping()
    public boolean changeEventOrganizer(int id, Employee employee) {
        return false;
    }

    @PostMapping("")
    public  ResponseEntity<?> addEvent(@RequestBody @Valid NewEventDto newEventDto) {  //Event?
        return eventService.insertNewEvent(newEventDto);
    }

    @PostMapping("/{id}/comments")
    public ResponseEntity<Comment> addCommentToEvent(@PathVariable int id, @RequestBody CommentDto commentDto) {
        return commentService.insertNewComment(id, commentDto);
    }

    //    @PostMapping()
    public ResponseEntity<Poll> addPollToEvent(PollTemplateDto pollTemplateDto) {
        return null;
    }

    //    @PostMapping()
    public ResponseEntity<PollAnswer> addPollAnswer(PollAnswerDto pollAnswerDto) {
        return null;
    }

    //    @PostMapping()
    public ResponseEntity<?> addGuestToEvent(AddGuestToEventDto addGuestToEventDto) {
        return null;
    }

    //    @PostMapping()
    public ResponseEntity<?> removeGuestFromEvent(int personId, int eventId) {
        return null;
    }

    @GetMapping("/{id}/join")
    @Operation(summary = "Add employee to event members",
    responses = {
            @ApiResponse(responseCode = "200", description = "Employee added to event", content = @Content(mediaType = "aplication/json", schema = @Schema(implementation = Event.class))),
            @ApiResponse(responseCode = "400", description = "Event is full or employee already on list", content = @Content(mediaType = "aplication/json", schema = @Schema(implementation = Event.class))),
            @ApiResponse(responseCode = "404", description = "Event or employee do not exist")
    })
    public ResponseEntity<?> joinEvent(@PathVariable("id") int id) {
        int employeeId = 1;//TODO take Id from Token
        if(employeeService.getEmployee(employeeId).getStatusCode()== HttpStatus.OK) {
            return eventService.addEmployeeToEvent(employeeId,id);
        }
        else{
            return ResponseEntity.status(404).body("Employee is not in DataBase");
        }
    }

    //    @PostMapping()
    public ResponseEntity<?> acceptInvite(int employeeId, int eventId) {
        return null;
    }

    //    @PostMapping()
    public ResponseEntity<?> filterEvent(String stringFilter) {
        return null;
    }

    //    @GetMapping
    public ResponseEntity<List<Person>> getAllInvited(int eventId) {
        return null;
    }

    @Transactional
    @PatchMapping("/{eventId}/members/{employeeId}")
    @Operation(summary = "Remove employee from event")
    public ResponseEntity<?> disjoinEvent(@PathVariable("employeeId") int employeeId,@PathVariable("eventId") int eventId) {
        return eventService.removeEmployeeFromEvent(employeeId,eventId);
    }
}
