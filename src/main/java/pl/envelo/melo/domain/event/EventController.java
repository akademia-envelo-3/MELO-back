package pl.envelo.melo.domain.event;


import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.envelo.melo.authorization.employee.EmployeeService;
import pl.envelo.melo.authorization.employee.dto.EmployeeNameDto;
import pl.envelo.melo.authorization.person.Person;
import pl.envelo.melo.authorization.person.PersonService;
import pl.envelo.melo.authorization.person.dto.AddGuestToEventDto;
import pl.envelo.melo.domain.hashtag.HashtagService;
import pl.envelo.melo.domain.location.LocationService;
import pl.envelo.melo.domain.poll.PollAnswer;
import pl.envelo.melo.domain.poll.PollService;
import pl.envelo.melo.domain.poll.dto.NewPollDto;
import pl.envelo.melo.domain.poll.dto.PollAnswerDto;
import pl.envelo.melo.domain.poll.dto.PollDto;
import pl.envelo.melo.domain.attachment.AttachmentService;
import pl.envelo.melo.domain.category.CategoryService;
import pl.envelo.melo.domain.comment.CommentService;
import pl.envelo.melo.domain.comment.dto.CommentDto;
import pl.envelo.melo.domain.event.dto.EventToDisplayOnListDto;
import pl.envelo.melo.domain.event.dto.NewEventDto;
import pl.envelo.melo.domain.poll.dto.*;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@RestController
@AllArgsConstructor
@Tag(name = "Event Controller")
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
    public ResponseEntity<?> editEvent(@PathVariable("id") int id, @RequestBody NewEventDto newEventDto) {
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
    public ResponseEntity<?> addEvent(@RequestPart(value = "eventData") @Parameter(schema = @Schema(type = "string", format = "binary")) @Valid NewEventDto newEventDto,
                                      @RequestPart(value = "mainPhoto", required = false) MultipartFile mainPhoto,
                                      @RequestPart(value = "additionalAttachments", required = false) MultipartFile[] additionalAttachments) {

        if (!Objects.isNull(additionalAttachments)) {
            if (additionalAttachments.length > 10) {
                return ResponseEntity.badRequest()
                        .body("You can upload max 10 attachments to Your Event");
            }
        }
        return eventService.insertNewEvent(newEventDto, mainPhoto, additionalAttachments);

    }

    @PostMapping(value = "/{id}/comments", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> addCommentToEvent(@PathVariable int id,
                                               @RequestPart(value = "commentData", required = false)
                                               @Parameter(schema = @Schema(type = "string", format = "binary")) CommentDto commentDto,
                                               @RequestPart(value = "attachments", required = false) MultipartFile[] multipartFiles) {
        if (!Objects.isNull(multipartFiles)) {
            if (multipartFiles.length > 10) {
                return ResponseEntity.badRequest()
                        .body("You can upload max 10 attachments to each Comment");
            }
        }
        return commentService.insertNewComment(id, commentDto, multipartFiles);
    }

    @PostMapping("/{event-id}/polls")
    @Operation(summary = "Add new poll to event",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Adds new poll to event successfully", content = {
                            @Content(mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = NewPollDto.class)))
                    }),
                    @ApiResponse(responseCode = "400", description = "Various validation with description: <br />" +
                            "PollQuestion must be between 2 and 1000 characters.<br />" +
                            "Poll must have between 2 and 10 pollAnswers.<br />" +
                            "PollAnswers cannot be repeatable. <br />Char amount of one pollAnswer must be between 1 and 255.<br />"),
                    @ApiResponse(responseCode = "404", description = "Event ID does not exist in database")
            })
    public ResponseEntity<?> addPollToEvent(@Valid @RequestBody NewPollDto newPollDto, @PathVariable("event-id") int id) {
        return pollService.insertNewPoll(newPollDto, id);

    }

    @GetMapping("/{event-id}/polls/{poll-id}")
    @Operation(summary = "Retrieve information about poll in event with given ID's",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Displays poll with given ID in event with given ID succesfully.<br />" +
                            "id + pollAnswer when Employee haven't voted yet.<br />" +
                            "result + pollAnswer when Employee already voted.", content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = PollDto.class))
                    }),
                    @ApiResponse(responseCode = "400", description = "Poll and Event with given ID are not correlated to each other."),
                    @ApiResponse(responseCode = "404", description = "Event ID and/or Poll ID does not exist in database")
            })
    public ResponseEntity<?> getPollFromEvent(@PathVariable("event-id") int eventId, @PathVariable("poll-id") int pollId, @RequestParam("employee-id") int employeeId) {
        return pollService.getPoll(eventId, pollId, employeeId);
    }

    @PostMapping("/{event-id}/polls/vote/{emp-id}")
    @Operation(summary = "Vote in poll",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Added new vote to poll", content = {
                            @Content(mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = PollResultDto.class)))
                    }),
                    @ApiResponse(responseCode = "400", description = "Various validation with description: <br />" +
                            "Employee already voted in this Poll.<br />" +
                            "PollAnswer must have at least one value.<br />" +
                            "This Poll is not multichoice, you can only put 1 PollAnswer.<br />"),
                    @ApiResponse(responseCode = "404", description = "Employee was not found in database.")
            })
    public ResponseEntity<?> addPollAnswer(@PathVariable("event-id") int eventId, @PathVariable("emp-id") int empId, @RequestBody PollSendResultDto pollSendResultDto) {
        return pollService.insertNewPollAnswer(eventId, empId, pollSendResultDto); // returns PollResultDto
    }


    @PostMapping("/{id}/external")
    @Operation(summary = "Send mail to guest with link to confirm participation on event ",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Mail was send"),
                    @ApiResponse(responseCode = "400", description = "Various validation with description: <br />" +
                            "Token was already created.<br />" +
                            "Event is not public, guest can't be added.<br />" +
                            "Event is full. <br />"+"Char amount of one pollAnswer must be between 1 and 255.<br />"),
                    @ApiResponse(responseCode = "404", description = "Event does not exist.<br/>"+
                            "Email was not send")
            })
    public ResponseEntity<?> addGuestToEvent(@Valid @RequestBody AddGuestToEventDto addGuestToEventDto, @PathVariable("id") int eventId) {
        return eventService.sendConfirmationMail(eventId, addGuestToEventDto);
    }


    @GetMapping("/participation")
    @Operation(summary = "Adding or removing guest from event.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Guest was added"),
                    @ApiResponse(responseCode = "400", description = "Various validation with description: <br />" +
                            "Person is still on list.<br />" +
                            "MailToken still exist.<br />" +
                            "Event is full. <br />"),
                    @ApiResponse(responseCode = "404", description = "Event does not exist.<br/>"+
                            "Email was not send.<br/>"+
                            "Token is not in database")
            })
    public ResponseEntity<?> toggleParticipation(@Valid @RequestParam("token") UUID token) {
        return eventService.toggleParticipation(token);
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
        return eventService.addEmployeeToEvent(employeeId, id);

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
