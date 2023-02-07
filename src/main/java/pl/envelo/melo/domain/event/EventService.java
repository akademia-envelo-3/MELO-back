package pl.envelo.melo.domain.event;

import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import jakarta.mail.Transport;
import jakarta.mail.internet.*;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.envelo.melo.authorization.employee.Employee;
import pl.envelo.melo.authorization.employee.EmployeeRepository;
import pl.envelo.melo.authorization.employee.EmployeeService;
import pl.envelo.melo.authorization.employee.dto.EmployeeDto;
import pl.envelo.melo.authorization.mailtoken.MailService;
import pl.envelo.melo.authorization.mailtoken.MailToken;
import pl.envelo.melo.authorization.mailtoken.MailTokenRepository;
import pl.envelo.melo.authorization.person.Person;
import pl.envelo.melo.authorization.person.PersonRepository;
import pl.envelo.melo.authorization.person.dto.AddGuestToEventDto;
import pl.envelo.melo.domain.event.dto.EventDetailsDto;
import pl.envelo.melo.domain.hashtag.Hashtag;
import pl.envelo.melo.domain.hashtag.HashtagService;
import pl.envelo.melo.domain.location.LocationRepository;
import pl.envelo.melo.domain.location.LocationService;
import pl.envelo.melo.domain.notification.NotificationService;
import pl.envelo.melo.domain.poll.PollAnswerRepository;
import pl.envelo.melo.domain.poll.PollRepository;
import pl.envelo.melo.domain.poll.PollService;
import pl.envelo.melo.domain.poll.dto.PollToDisplayOnListDto;
import pl.envelo.melo.domain.unit.UnitRepository;
import pl.envelo.melo.domain.attachment.Attachment;
import pl.envelo.melo.domain.attachment.AttachmentRepository;
import pl.envelo.melo.domain.attachment.AttachmentService;
import pl.envelo.melo.domain.attachment.AttachmentType;
import pl.envelo.melo.domain.category.CategoryRepository;
import pl.envelo.melo.domain.comment.CommentRepository;
import pl.envelo.melo.domain.event.dto.EventToDisplayOnListDto;
import pl.envelo.melo.domain.event.dto.NewEventDto;
import pl.envelo.melo.domain.hashtag.HashtagRepository;

import pl.envelo.melo.mappers.*;
import pl.envelo.melo.validators.EventValidator;

import java.time.LocalDateTime;
import java.util.*;

@Service
@AllArgsConstructor
public class EventService {
    //Services
    private final HashtagService hashtagService;
    private final NotificationService notificationService;
    private final EmployeeService employeeService;
    private final LocationService locationService;
    private final PollService pollService;
    private AttachmentService attachmentService;
    private MailService mailService;
    //Repositories
    private final EventRepository eventRepository;
    private final EmployeeRepository employeeRepository;
    private final HashtagRepository hashtagRepository;
    private final CategoryRepository categoryRepository;
    private final AttachmentRepository attachmentRepository;
    private final LocationRepository locationRepository;
    private final UnitRepository unitRepository;
    private final PollRepository pollRepository;
    private final PollAnswerRepository pollAnswerRepository;
    private final CommentRepository commentRepository;
    private final PersonRepository personRepository;
    private final MailTokenRepository mailTokenRepository;
    //Mappers
    private EventDetailsMapper eventDetailsMapper;
    private final PollToDisplayOnListDtoMapper pollToDisplayOnListDtoMapper;
    private EventMapper eventMapper;
    private HashtagMapper hashtagMapper;
    private EventEditMapper eventEditMapper;
    private AttachmentMapper attachmentMapper;
    private EmployeeMapper employeeMapper;
    private AddGuestToEventMapper addGuestToEventMapper;
    //Other
    private EventValidator eventValidator;
    private EditEventNotificationHandler eventNotificationHandler;
    private EventUpdater eventUpdater;
    public ResponseEntity<?> getEvent(int id, Integer employeeId) {
        if (eventRepository.existsById(id)) {
            Event event = eventRepository.findById(id).get();
            EventDetailsDto eventDetailsDto = eventDetailsMapper.convert(event);
            if(eventDetailsDto.getPolls() != null) {
                Set<PollToDisplayOnListDto> pollSet = new HashSet<>();

                event.getPolls().stream()
                        .map(poll -> {
                            PollToDisplayOnListDto dto = pollToDisplayOnListDtoMapper.convert(poll);
                            dto.setFilled(pollService.employeeOnLists(poll, employeeId));
                            return dto;
                        })
                        .forEach(pollSet::add);
                 eventDetailsDto.setPolls(pollSet);
            }

            return ResponseEntity.ok(eventDetailsDto);
        } else {
            return ResponseEntity.status(404).body("Event with this ID does not exist");
        }
    }

    public ResponseEntity<List<EventToDisplayOnListDto>> listAllEvents() {
        List<Event> result = eventRepository.findAllByStartTimeAfterAndType(LocalDateTime.now(), EventType.LIMITED_EXTERNAL);
        result.addAll(eventRepository.findAllByStartTimeAfterAndType(LocalDateTime.now(), EventType.UNLIMITED_EXTERNAL));
        result.addAll(eventRepository.findAllByStartTimeAfterAndType(LocalDateTime.now(), EventType.UNLIMITED_PUBLIC_INTERNAL));
        result.addAll(eventRepository.findAllByStartTimeAfterAndType(LocalDateTime.now(), EventType.LIMITED_PUBLIC_INTERNAL));
        result.sort(Comparator.comparing(Event::getStartTime));
        return ResponseEntity.ok(result.stream().map(eventMapper::convert).toList());
    }

    @Transactional
    public ResponseEntity<?> insertNewEvent(NewEventDto newEventDto, MultipartFile mainPhoto, MultipartFile[] additionalAttachments) {
        Event event = eventMapper.newEvent(newEventDto);

        //validation
        if (event.getType().toString().startsWith("LIMITED")) {
            if (event.getMemberLimit() < 1) {
                return ResponseEntity.status(400).body("Event with limited eventType must have higher memberLimit than 0.");
            }
        }

        if (newEventDto.getLocation() != null) {
            event.setLocation(locationService.insertOrGetLocation(newEventDto.getLocation()));
        }

        if (employeeRepository.existsById(newEventDto.getOrganizerId())) {
            event.setOrganizer(employeeRepository.findById(newEventDto.getOrganizerId()).get());
        }

        if (!(newEventDto.getCategoryId() == null)) {
            if (categoryRepository.findById(newEventDto.getCategoryId()).isPresent()) {
                event.setCategory(categoryRepository.findById(newEventDto.getCategoryId()).get());
            } else {
                event.setCategory(null); // todo set category
            }
        }

        if (!Objects.isNull(additionalAttachments)) {
            /// Wysyłam, przetwarzam kolejne załączniki i dodaję do eventu.
            for (MultipartFile multipartFile : additionalAttachments) {
                AttachmentType attachmentType = attachmentService.validateAttachmentType(multipartFile);
                if(Objects.isNull(attachmentType)) {
                    return ResponseEntity.badRequest()
                            .body("Illegal format of attachment. WTF ARE U DOING? TURBO ERROR!");
                }
            }


            for (MultipartFile multipartFile : additionalAttachments) {
                Attachment attachmentFromServer = attachmentService.uploadFileAndSaveAsAttachment(multipartFile);
                if (attachmentFromServer == null) {
                    return ResponseEntity.badRequest()
                            .body("Illegal format of attachment. WTF ARE U DOING?");
                }
                if(Objects.isNull(event.getAttachments())) {
                    event.setAttachments(new HashSet<>());
                }
                event.getAttachments().add(attachmentFromServer);
            }
        }
        /// Set Main Photo
        if (!Objects.isNull(mainPhoto)) {

            Attachment mainPhotoFromServer = attachmentService.uploadFileAndSaveAsAttachment(mainPhoto);
            if (mainPhotoFromServer == null) {
                return ResponseEntity.badRequest()
                        .body("Illegal format of attachment. WTF ARE U DOING?");
            }
            if (mainPhotoFromServer.getAttachmentType() != AttachmentType.PHOTO) {
                return ResponseEntity.badRequest()
                        .body("Illegal format of event Photo!");
            }
            event.setMainPhoto(mainPhotoFromServer);

        } else {
            event.setMainPhoto(null); //todo swap with attachmentMainPhoto method
        }


        if (!(newEventDto.getHashtags() == null)) {
            for (Hashtag hashtag : event.getHashtags()) {
                hashtagRepository.save(hashtag);
                //todo swap with hashtagService method when present
            }
        }

        if (!(newEventDto.getUnitId() == null)) {
            if (unitRepository.findById(newEventDto.getUnitId()).isPresent()) {
                event.setUnit(unitRepository.findById(newEventDto.getUnitId()).get());
            } else {
                event.setUnit(null);
            }
        } //todo create UnitMapper and use UnitRepository to find unit in database


        return new ResponseEntity(eventRepository.save(event), HttpStatus.CREATED);
    }

    public ResponseEntity<EmployeeDto> getEventOrganizer(int id) {
        return null;
    }

    public ResponseEntity<?> changeEventOrganizer(int eventId, int employeeId) {

        int currentTokenId = 1;
        Employee employee;
        if (!eventRepository.existsById(eventId)) {
            return ResponseEntity.status(404).body("Event with Id " + eventId + " does not exist");
        } else if (!employeeRepository.existsById(employeeId)) {
            return ResponseEntity.status(404).body("Employee with Id " + employeeId + " does not exist");
        }else if (currentTokenId != eventRepository.findById(eventId).get().getOrganizer().getId()){
            return ResponseEntity.status(401).body("You are not the organizer of the event you " +
                                                        "do not have the authority to make changes");
        }else if (employeeId == eventRepository.findById(eventId).get().getOrganizer().getId()){
            return ResponseEntity.status(400).body("You are event organizer already");
        } else {
            employee = employeeRepository.getReferenceById(employeeId);
            Event event = eventRepository.findById(eventId).get();

            employeeService.removeFromOwnedEvents(event.getOrganizer().getId(), event);
            event.setOrganizer(employee);
            employeeService.addToOwnedEvents(employeeId, event);
            employeeService.addToJoinedEvents(employeeId, event);
            eventRepository.findById(eventId).get()
                    .getMembers()
                    .add(employee.getUser().getPerson());

            return ResponseEntity.status(200).body("The organizer of the event with id "
                    + eventId + " has been correctly changed to "
                    + employee.getUser().getPerson().getFirstName() + " "
                    + employee.getUser().getPerson().getLastName());
        }
    }

    public ResponseEntity<?> updateEvent(int id, NewEventDto newEventDto) {
        //TODO dostosować do funckjonalnosci wysyłania plików na serwer
        Optional<Event> optionalEvent = eventRepository.findById(id);
        if (optionalEvent.isEmpty())
            return ResponseEntity.badRequest().body("Event with id " + id + " not found");
        Event event = optionalEvent.get();

        Map<String, String> validationResult = eventValidator.validateToEdit(event, newEventDto);
        validationResult.forEach((k, v) -> System.out.println(k + " " + v));
        if (validationResult.size() != 0) {
            return ResponseEntity.badRequest().body(validationResult);
        }
        eventUpdater.update(event, newEventDto);
        eventNotificationHandler.editNotification(event, newEventDto).forEach(notificationService::insertEventNotification);
        return ResponseEntity.ok(eventDetailsMapper.convert(eventRepository.save(event)));
    }

    public ResponseEntity<?> editEventForm(int id) {
        if (!eventRepository.existsById(id))
            return ResponseEntity.status(HttpStatusCode.valueOf(404)).body("Event with " + id + " does not exists");
        return ResponseEntity.ok(eventEditMapper.convert(eventRepository.getReferenceById(id)));
    }

    @Transactional
    public ResponseEntity<?> addEmployeeToEvent(int employeeId, int eventId) { //void?
        if (employeeRepository.existsById(employeeId)) {
            Employee employee = employeeRepository.findById(employeeId).get();
            Optional<Event> event = eventRepository.findById(eventId);
            if (event.isPresent()) {
                if (event.get().getType().toString().startsWith("LIMITED")) {
                    if (event.get().getMembers().size() >= event.get().getMemberLimit().intValue()) {
                        return ResponseEntity.status(400).body("Event is full");
                    }
                }
                if (employeeService.addToJoinedEvents(employeeId, event.get())) {
                    Set<Person> eventMembers = event.get().getMembers();
                    if (eventMembers == null) {
                        eventMembers = new HashSet<>();
                        eventMembers.add(employee.getUser().getPerson());
                        event.get().setMembers(eventMembers);
                    } else {
                        event.get().getMembers().add(employee.getUser().getPerson());
                    }
                    return ResponseEntity.ok(eventMapper.convert(event.get()));
                } else {
                    return ResponseEntity.status(400).body("Employee already on list");
                }

            }
            return ResponseEntity.status(404).body("Event does not exist");
        } else {
            return ResponseEntity.status(404).body("Employee is not in database");
        }
    }

    public ResponseEntity<?> removeEmployeeFromEvent(int employeeId, int eventId) {

        if (!employeeRepository.existsById(employeeId)) {

            return ResponseEntity.status(404).body("Employee with Id " + employeeId + " does not exist");

        } else if (!eventRepository.existsById(eventId)) {

            return ResponseEntity.status(404).body("Event with Id " + eventId + " does not exist");

        } else if (!eventRepository.findById(eventId).get()
                .getMembers()
                .contains(employeeRepository.findById(employeeId).get().getUser().getPerson())) {

            return ResponseEntity.status(404).body("Employee with Id " + employeeId + " is not a member of this event");

        } else if (eventRepository.findById(eventId).get().getOrganizer().getId() == employeeId) {

            return ResponseEntity.status(403).body("Event organizer cant be remove from his event");

        } else {
            eventRepository.findById(eventId).get()
                    .getMembers()
                    .remove(employeeRepository
                            .findById(employeeId).get()
                            .getUser()
                            .getPerson());
            employeeService.removeFromJoinedEvents(employeeId, eventRepository.findById(eventId).get());
            return ResponseEntity.status(200).body("Successfully removed an employee with Id "
                    + employeeId + " from the event with Id" + eventId);
        }
    }

    public ResponseEntity<?> toggleParticipation(UUID token){
        Optional<MailToken> mailToken = mailTokenRepository.findById(token);
        if(!mailToken.isPresent()){
            return ResponseEntity.status(404).body("Token is not in database");
        }
        Event event = mailToken.get().getEvent();
        if(eventRepository.findById(event.getId()).isPresent()){
            Person person = mailToken.get().getPerson();
            String email = person.getEmail();
            if(event.getMembers().contains(person)){
                event.getMembers().remove(person);
                mailTokenRepository.delete(mailToken.get());
                if(event.getMembers().contains(person)){
                    return ResponseEntity.status(400).body("Person is still on list");
                }
                if(mailTokenRepository.existsById(mailToken.get().getToken())){
                    return ResponseEntity.status(400).body("MailToken still exist");
                }
                return ResponseEntity.ok("Person removed successful");
            }
            if (event.getType().toString().startsWith("LIMITED")) {
                if (event.getMembers().size() >= event.getMemberLimit().intValue()) {
                    return ResponseEntity.status(400).body("Event is full");
                }
            }
            event.getMembers().add(person);
            eventRepository.save(event);
            mailTokenRepository.delete(mailToken.get());
            return sendResignationTokenMail(event,person);
        }

        return ResponseEntity.status(404).body("Event does not exist");
    }

    public ResponseEntity<?> sendConfirmationMail(int eventId, AddGuestToEventDto addGuestToEventDto){
        Person person = addGuestToEventMapper.toEntity(addGuestToEventDto);
        if(!eventRepository.findById(eventId).isPresent()){
            return ResponseEntity.status(404).body("Event does not exist");
        }
        Event event = eventRepository.findById(eventId).get();

        Optional<List<MailToken>> mailTokenList = mailTokenRepository.findAllByEvent(event);
        if(mailTokenList.isPresent()){
            Person finalPerson = person;
            if(mailTokenList.get().stream().anyMatch(mailToken -> Objects.equals(mailToken.getPerson().getEmail(), finalPerson.getEmail()))){
                return ResponseEntity.status(400).body("Token was already created");
            }
        }
        if(!event.getType().toString().contains("PUBLIC")){
            return ResponseEntity.status(400).body("Event is not public, guest can't be added.");
        }
        if (event.getType().toString().startsWith("LIMITED")) {
            if (event.getMembers().size() >= event.getMemberLimit().intValue()) {
                return ResponseEntity.status(400).body("Event is full");
            }
        }
        personRepository.save(person);
        if(mailService.sendMailWithToken(person,event, true)){
            return ResponseEntity.ok("Email was send");
        }
        return ResponseEntity.status(404).body("Email was not send");
    }
    public ResponseEntity<?> sendResignationTokenMail(Event event, Person person){
        if(mailService.sendMailWithToken(person,event, false )){
            return ResponseEntity.ok("Email was send");
        }
        return ResponseEntity.status(404).body("Email was not send");
    }

}