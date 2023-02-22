package pl.envelo.melo.domain.event;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import net.kaczmarzyk.spring.data.jpa.domain.Between;
import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.domain.In;
import net.kaczmarzyk.spring.data.jpa.domain.Like;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.envelo.melo.authorization.employee.dto.EmployeeNameDto;
import pl.envelo.melo.authorization.person.Person;
import pl.envelo.melo.authorization.person.dto.AddGuestToEventDto;
import pl.envelo.melo.domain.comment.CommentService;
import pl.envelo.melo.domain.comment.dto.CommentDto;
import pl.envelo.melo.domain.event.dto.EventToDisplayOnListDto;
import pl.envelo.melo.domain.event.dto.NewEventDto;
import pl.envelo.melo.domain.event.utils.PagingHeaders;
import pl.envelo.melo.domain.event.utils.PagingResponse;
import pl.envelo.melo.domain.poll.PollService;
import pl.envelo.melo.domain.poll.dto.NewPollDto;
import pl.envelo.melo.domain.poll.dto.PollDto;
import pl.envelo.melo.domain.poll.dto.PollResultDto;
import pl.envelo.melo.domain.poll.dto.PollSendResultDto;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@RestController
@AllArgsConstructor
@Tag(name = "Event Controller")
@RequestMapping("/v1/events")
public class EventController {

    private final EventService eventService;
    private final PollService pollService;
    private final CommentService commentService;

    @Transactional
    @PreAuthorize("hasAnyAuthority(@securityConfiguration.getAdminRole(), @securityConfiguration.getEmployeeRole())")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<EventToDisplayOnListDto>> get(
            @And({
                    @Spec(path = "name", params = "name", spec = Like.class),
                    @Spec(path = "organizer.id", params = "organizer", spec = Equal.class),
                    @Spec(path = "hashtags.content", params = "hashtags", spec = In.class),
                    @Spec(path = "type", params = "type", spec = Equal.class),
                    @Spec(path = "startTime", params = {"startTime", "endTime"}, spec = Between.class),
                    @Spec(path = "category.name", params = "category", spec = Equal.class)
            }) Specification<Event> spec,
            Sort sort,
            @RequestHeader HttpHeaders headers) {
        final PagingResponse response = eventService.get(spec, headers, sort);
        return new ResponseEntity<>(response.getElements(), returnHttpHeaders(response), HttpStatus.OK);
    }

    public HttpHeaders returnHttpHeaders(PagingResponse response) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(PagingHeaders.COUNT.getName(), String.valueOf(response.getCount()));
        headers.set(PagingHeaders.PAGE_SIZE.getName(), String.valueOf(response.getPageSize()));
        headers.set(PagingHeaders.PAGE_OFFSET.getName(), String.valueOf(response.getPageOffset()));
        headers.set(PagingHeaders.PAGE_NUMBER.getName(), String.valueOf(response.getPageNumber()));
        headers.set(PagingHeaders.PAGE_TOTAL.getName(), String.valueOf(response.getPageTotal()));
        return headers;
    }

//    @GetMapping()
//    public ResponseEntity<List<EventToDisplayOnListDto>> getEvents() {
//        return eventService.listAllEvents();
//    }

    @PreAuthorize("hasAuthority(@securityConfiguration.getEmployeeRole())")
    @PatchMapping(value = "/{eventId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> editEvent(@PathVariable("eventId") int id,
                                       @RequestPart(value = "update") @Parameter(schema = @Schema(type = "string", format = "binary")) Map<String, Map<String, Object>> update,
                                       @RequestPart(value = "mainPhoto", required = false) MultipartFile mainPhoto,
                                       @RequestPart(value = "additionalAttachments", required = false) MultipartFile[] additionalAttachments,
                                       Principal principal) {
        return eventService.updateEvent(id, update.get("updates"), update.get("adds"), update.get("deletes"), mainPhoto, additionalAttachments, principal);
    }

    @PreAuthorize("hasAuthority(@securityConfiguration.getEmployeeRole())")
    @GetMapping("/{id}/edit-form")
    public ResponseEntity<?> editForm(@RequestParam("id") int id, Principal principal) {
        return eventService.editEventForm(id, principal);
    }

    //    @GetMapping()
    @PreAuthorize("hasAnyAuthority(@securityConfiguration.getAdminRole(), @securityConfiguration.getEmployeeRole())")
    @GetMapping("/{id}")
    public ResponseEntity<?> getEvent(@PathVariable("id") int id, Principal principal) {
        return eventService.getEvent(id, principal);
    }

    @PreAuthorize("hasAuthority(@securityConfiguration.getEmployeeRole())")
    //    @GetMapping()
    public ResponseEntity<List<Person>> getEventMembers() {
        return null;
    }

    @PreAuthorize("hasAnyAuthority(@securityConfiguration.getAdminRole(), @securityConfiguration.getEmployeeRole())")
    //    @GetMapping()/
    public ResponseEntity<EmployeeNameDto> getEventOrganizer(int id) {
        return null;
    }

    @PreAuthorize("hasAuthority(@securityConfiguration.getEmployeeRole())")
    @Transactional
    @PatchMapping("/{id}/organizer")
    @Operation(summary = "Change event organizer from current to another")
    public ResponseEntity<?> changeEventOrganizer(@PathVariable("id") int eventId, @RequestParam("newOrganizerId") int newOrganizerId, Principal principal) {
        return eventService.changeEventOrganizer(eventId, newOrganizerId, principal);
    }

    @PreAuthorize("hasAuthority(@securityConfiguration.getEmployeeRole())")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    //@io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(encoding = @Encoding(name = "eventData", contentType = "application/json")))
    //W razie problemów na froncie, pokombinować z enkodowaniem. v2.0 Swaggera, nie wspiera, więc poniższe rozwiązanie wymaga w Swaggerze, uploadu JSON w formie
    //pliku .json. W PostMan można wysłać zarówno plik jak i json "tekstowy" z parametrem Content-Type application/json.
    public ResponseEntity<?> addEvent(@RequestPart(value = "eventData") @Parameter(schema = @Schema(type = "string", format = "binary")) @Valid NewEventDto newEventDto,
                                      @RequestPart(value = "mainPhoto", required = false) MultipartFile mainPhoto,
                                      @RequestPart(value = "additionalAttachments", required = false) MultipartFile[] additionalAttachments,
                                      Principal principal) {

        if (!Objects.isNull(additionalAttachments)) {
            if (additionalAttachments.length > 10) {
                return ResponseEntity.badRequest()
                        .body("You can upload max 10 attachments to Your Event");
            }
        }
        return eventService.insertNewEvent(newEventDto, mainPhoto, additionalAttachments, principal);

    }

    @PreAuthorize("hasAuthority(@securityConfiguration.getEmployeeRole())")
    @PostMapping(value = "/{id}/comments", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> addCommentToEvent(@PathVariable("id") int id,
                                               @RequestPart(value = "commentData", required = false)
                                               @Parameter(schema = @Schema(type = "string", format = "binary")) CommentDto commentDto,
                                               @RequestPart(value = "attachments", required = false) MultipartFile[] multipartFiles,
                                               Principal principal
    ) {
        if (!Objects.isNull(multipartFiles)) {
            if (multipartFiles.length > 10) {
                return ResponseEntity.badRequest()
                        .body("You can upload max 10 attachments to each Comment");
            }
        }
        return commentService.insertNewComment(id, commentDto, multipartFiles, principal);
    }

    @PreAuthorize("hasAuthority(@securityConfiguration.getEmployeeRole())")
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
    public ResponseEntity<?> addPollToEvent(@Valid @RequestBody NewPollDto newPollDto, @PathVariable("event-id") int id, Principal principal) {
        return pollService.insertNewPoll(newPollDto, id, principal);

    }

    @PreAuthorize("hasAuthority(@securityConfiguration.getEmployeeRole())")
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
    public ResponseEntity<?> getPollFromEvent(@PathVariable("event-id") int eventId, @PathVariable("poll-id") int pollId, Principal principal) {
        return pollService.getPoll(eventId, pollId, principal);
    }

    @PreAuthorize("hasAuthority(@securityConfiguration.getEmployeeRole())")
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
    public ResponseEntity<?> addPollAnswer(@PathVariable("event-id") int eventId, @RequestBody PollSendResultDto pollSendResultDto, Principal principal) {
        return pollService.insertNewPollAnswer(eventId, pollSendResultDto, principal); // returns PollResultDto
    }

    @PostMapping("/{id}/external")
    @Operation(summary = "Send mail to guest with link to confirm participation on event ",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Mail was send"),
                    @ApiResponse(responseCode = "400", description = "Various validation with description: <br />" +
                            "Token was already created.<br />" +
                            "Event is not public, guest can't be added.<br />" +
                            "Event is full. <br />" + "Char amount of one pollAnswer must be between 1 and 255.<br />"),
                    @ApiResponse(responseCode = "404", description = "Event does not exist.<br/>" +
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
                    @ApiResponse(responseCode = "404", description = "Event does not exist.<br/>" +
                            "Email was not send.<br/>" +
                            "Token is not in database")
            })
    public ResponseEntity<?> toggleParticipation(@Valid @RequestParam("token") UUID token) {
        return eventService.toggleParticipation(token);
    }

    @PreAuthorize("hasAuthority(@securityConfiguration.getEmployeeRole())")
    @GetMapping("/{id}/join")
    @Operation(summary = "Add employee to event members",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Employee added to event", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Event.class))),
                    @ApiResponse(responseCode = "400", description = "Event is full or employee already on list", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Event.class))),
                    @ApiResponse(responseCode = "404", description = "Event or employee do not exist")
            })
    public ResponseEntity<?> joinEvent(@PathVariable("id") int id, Principal principal) {
        return eventService.addEmployeeToEvent(id, principal);

    }

    @PreAuthorize("hasAuthority(@securityConfiguration.getEmployeeRole())")
    //    @PostMapping()
    public ResponseEntity<?> acceptInvite(int employeeId, int eventId) {
        return null;
    }

    //    @PostMapping()
    public ResponseEntity<?> filterEvent(String stringFilter) {
        return null;
    }

    @PreAuthorize("hasAuthority(@securityConfiguration.getEmployeeRole())")
    //    @GetMapping
    public ResponseEntity<List<Person>> getAllInvited(int eventId) {
        return null;
    }

    @PreAuthorize("hasAuthority(@securityConfiguration.getEmployeeRole())")
    @Transactional
    @PatchMapping("/{eventId}/members")
    @Operation(summary = "Remove employee from event")
    public ResponseEntity<?> disjoinEvent(@PathVariable("eventId") int eventId, Principal principal) {
        return eventService.removeEmployeeFromEvent(eventId, principal);
    }
}
