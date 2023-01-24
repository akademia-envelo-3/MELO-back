package pl.envelo.melo.domain.event;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Encoding;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
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
import java.util.Objects;

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

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    //@io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(encoding = @Encoding(name = "eventData", contentType = "application/json")))
    //W razie problemów na froncie, pokombinować z enkodowaniem. v2.0 Swaggera, nie wspiera, więc poniższe rozwiązanie wymaga w Swaggerze, uploadu JSON w formie
    //pliku .json. W PostMan można wysłać zarówno plik jak i json "tekstowy" z parametrem Content-Type application/json.
    public ResponseEntity<?> addEvent(@RequestPart(value = "eventData") @Parameter(schema =@Schema(type = "string", format = "binary")) NewEventDto newEventDto,
                                       @RequestPart(value = "mainPhoto", required = false) MultipartFile mainPhoto,
                                       @RequestPart(value = "additionalAttachments", required = false) MultipartFile[] additionalAttachments) {
        if(!Objects.isNull(additionalAttachments)) {
            if(additionalAttachments.length > 10) {
                return ResponseEntity.badRequest()
                        .body("You can upload max 10 attachments to Your Event");
            }
        }

        return eventService.insertNewEvent(newEventDto, mainPhoto, additionalAttachments);
    }

    @PostMapping(value = "/{id}/comments" , consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> addCommentToEvent(@PathVariable int id,
                                                     @RequestPart(value = "commentData" , required = false)
                                                     @Parameter(schema =@Schema(type = "string", format = "binary")) CommentDto commentDto,
                                                     @RequestPart(value = "attachments", required = false) MultipartFile[] multipartFiles) {
        if(!Objects.isNull(multipartFiles)) {
            if(multipartFiles.length > 10) {
                return ResponseEntity.badRequest()
                        .body("You can upload max 10 attachments to each Comment");
            }
        }
        return commentService.insertNewComment(id, commentDto, multipartFiles);
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

    //    @PostMapping()
    public ResponseEntity<?> joinEvent(int employeeId, int eventId) {
        return null;
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

    //    @PostMapping()
    public ResponseEntity<?> disjoinEvent(int employeeId, int eventId) {
        return null;
    }
}
