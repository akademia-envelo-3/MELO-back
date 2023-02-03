package pl.envelo.melo.domain.event;


import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Encoding;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.transaction.Transactional;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.envelo.melo.authorization.employee.Employee;
import pl.envelo.melo.authorization.employee.EmployeeService;
import pl.envelo.melo.authorization.employee.dto.EmployeeDto;
import pl.envelo.melo.authorization.employee.dto.EmployeeNameDto;
import pl.envelo.melo.authorization.person.Person;
import pl.envelo.melo.authorization.person.PersonService;
import pl.envelo.melo.authorization.person.dto.AddGuestToEventDto;
import pl.envelo.melo.domain.attachment.AttachmentService;
import pl.envelo.melo.domain.category.Category;
import pl.envelo.melo.domain.category.CategoryService;
import pl.envelo.melo.domain.comment.Comment;
import pl.envelo.melo.domain.comment.CommentService;
import pl.envelo.melo.domain.comment.dto.CommentDto;
import pl.envelo.melo.domain.event.dto.EventDetailsDto;
import pl.envelo.melo.domain.event.dto.EventToDisplayOnListDto;
import pl.envelo.melo.domain.event.dto.NewEventDto;
import pl.envelo.melo.domain.hashtag.HashtagService;
import pl.envelo.melo.domain.location.LocationService;
import pl.envelo.melo.domain.poll.PollAnswer;
import pl.envelo.melo.domain.poll.PollService;
import pl.envelo.melo.domain.poll.dto.NewPollDto;
import pl.envelo.melo.domain.poll.dto.PollAnswerDto;
import pl.envelo.melo.domain.poll.dto.PollDto;

import java.util.List;
import java.util.Objects;

@RestController
@AllArgsConstructor
@RequestMapping("/v1/events")
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
    public ResponseEntity<?> editForm(@RequestParam("id") int id) {
        return eventService.editEventForm(id);
    }
    //    @GetMapping()

    @GetMapping("/{id}")
    public ResponseEntity<?> getEvent(@RequestParam("id") int id, @RequestParam("employeeId") Integer employeeId) {
        return eventService.getEvent(id, employeeId);
    }

    //    @GetMapping()
    public ResponseEntity<List<Person>> getEventMembers() {
        return null;
    }

    //    @GetMapping()/
    public ResponseEntity<EmployeeNameDto> getEventOrganizer(int id) {
        return null;
    }

    @Transactional
    @PatchMapping("/{id}/organizer")
    @Operation(summary = "Change event organizer from current to another")
    public ResponseEntity<?> changeEventOrganizer(@PathVariable("id") int eventId, @RequestBody int employeeId) {
        return eventService.changeEventOrganizer(eventId, employeeId);
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

    @PostMapping("/{id}/polls")
    @Operation(summary = "Add new poll to event",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Adds new poll to event successfully", content = {
                            @Content(mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = PollDto.class)))
                    }),
                    @ApiResponse(responseCode = "400", description = "Various validation with description: <br />" +
                            "PollQuestion must be between 2 and 1000 characters.<br />" +
                            "Poll must have between 2 and 10 pollAnswers.<br />" +
                            "PollAnswers cannot be repeatable. <br />Char amount of one pollAnswer must be between 1 and 255.<br />"),
                    @ApiResponse(responseCode = "404", description = "Event ID does not exist in database")
            })
    public ResponseEntity<?> addPollToEvent(@Valid @RequestBody NewPollDto newPollDto, @PathVariable("id") int id) {
        return pollService.insertNewPoll(newPollDto, id);

    }

    @GetMapping("/{id}/polls/{poll-id}")
    @Operation(summary = "Retrieve information about poll in event with given ID's",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Displays poll with given ID in event with given ID successfully.", content = {
                            @Content(mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = PollDto.class)))
                    }),
                    @ApiResponse(responseCode = "400", description = "Poll and Event with given ID are not correlated to each other."),
                    @ApiResponse(responseCode = "404", description = "Event ID and/or Poll ID does not exist in database")
            })
    public ResponseEntity<?> getPollFromEvent(@PathVariable("id") int id, @PathVariable("poll-id") int pollId) {
        return pollService.getPoll(id, pollId);
    }

    //        @PostMapping()
    public ResponseEntity<PollAnswer> addPollAnswer(PollAnswerDto pollAnswerDto) {
        return null;
    }

    @PostMapping("/{id}/external")
    public ResponseEntity<?> addGuestToEvent(@PathVariable("id") int eventId, AddGuestToEventDto addGuestToEventDto) {
        //TODO create person
        return null;
    }



    //    @PostMapping()
    public ResponseEntity<?> removeGuestFromEvent(int personId, int eventId) {
        return null;
    }

    @GetMapping("/{id}/join/{employeeId}")
    @Operation(summary = "Add employee to event members",
    responses = {
            @ApiResponse(responseCode = "200", description = "Employee added to event", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Event.class))),
            @ApiResponse(responseCode = "400", description = "Event is full or employee already on list", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Event.class))),
            @ApiResponse(responseCode = "404", description = "Event or employee do not exist")
    })
    public ResponseEntity<?> joinEvent(@PathVariable("id") int id, @PathVariable("employeeId") int employeeId) {
//        int employeeId = 2;//TODO take Id from Token
            return eventService.addEmployeeToEvent(employeeId,id);

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
    public ResponseEntity<?> disjoinEvent(@PathVariable("employeeId") int employeeId, @PathVariable("eventId") int eventId) {
        return eventService.removeEmployeeFromEvent(employeeId, eventId);
    }
}
