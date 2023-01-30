package pl.envelo.melo.domain.event;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.envelo.melo.authorization.employee.Employee;
import pl.envelo.melo.authorization.employee.EmployeeRepository;
import pl.envelo.melo.authorization.employee.EmployeeService;
import pl.envelo.melo.authorization.employee.dto.EmployeeDto;
import pl.envelo.melo.authorization.employee.dto.EmployeeNameDto;
import pl.envelo.melo.authorization.person.Person;
import pl.envelo.melo.authorization.person.PersonRepository;
import pl.envelo.melo.domain.attachment.Attachment;
import pl.envelo.melo.domain.attachment.AttachmentRepository;
import pl.envelo.melo.domain.category.CategoryRepository;
import pl.envelo.melo.domain.comment.CommentRepository;
import pl.envelo.melo.domain.event.dto.EventDetailsDto;
import pl.envelo.melo.domain.event.dto.EventToDisplayOnListDto;
import pl.envelo.melo.domain.event.dto.NewEventDto;
import pl.envelo.melo.domain.hashtag.Hashtag;
import pl.envelo.melo.domain.hashtag.HashtagRepository;
import pl.envelo.melo.domain.hashtag.HashtagService;
import pl.envelo.melo.domain.location.LocationRepository;
import pl.envelo.melo.domain.location.LocationService;
import pl.envelo.melo.domain.notification.NotificationService;
import pl.envelo.melo.domain.poll.*;
import pl.envelo.melo.domain.poll.dto.PollToDisplayOnListDto;
import pl.envelo.melo.domain.unit.UnitRepository;

import pl.envelo.melo.mappers.*;
import pl.envelo.melo.validators.EventValidator;

import java.time.LocalDateTime;
import java.util.*;

@Service
@AllArgsConstructor
public class EventService {

    private final HashtagService hashtagService;
    private final NotificationService notificationService;
    private EventDetailsMapper eventDetailsMapper;
    private final EventRepository eventRepository;
    private final EmployeeRepository employeeRepository;
    private final EmployeeService employeeService;
    private final HashtagRepository hashtagRepository;
    private final CategoryRepository categoryRepository;
    private final AttachmentRepository attachmentRepository;
    private final LocationRepository locationRepository;
    private final LocationService locationService;
    private final PollService pollService;
    private final UnitRepository unitRepository;
    private final PollRepository pollRepository;
    private final PollAnswerRepository pollAnswerRepository;
    private final CommentRepository commentRepository;
    private final PersonRepository personRepository;
    private final PollToDisplayOnListDtoMapper pollToDisplayOnListDtoMapper;
    private EventMapper eventMapper;
    private HashtagMapper hashtagMapper;
    private EventEditMapper eventEditMapper;
    private AttachmentMapper attachmentMapper;
    private EventUpdater eventUpdater;
    private EventValidator eventValidator;
    private EmployeeMapper employeeMapper;
    private EditEventNotificationHandler eventNotificationHandler;

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
            return ResponseEntity.status(404).body("Event with this ID do not exist");
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

    public ResponseEntity<?> insertNewEvent(NewEventDto newEventDto) {  //void?

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

        if (!(event.getMainPhoto() == null)) {
            attachmentRepository.save(event.getMainPhoto());
        } else {
            event.setMainPhoto(null); //todo swap with attachmentMainPhoto method
        }


        if (!(newEventDto.getCategoryId() == null)) {
            if (categoryRepository.findById(newEventDto.getCategoryId()).isPresent()) {
                event.setCategory(categoryRepository.findById(newEventDto.getCategoryId()).get());
            } else {
                event.setCategory(null); // todo set category
            }
        }

        if (!(newEventDto.getAttachments() == null)) {
            for (Attachment attachment : event.getAttachments()) {
                attachmentRepository.save(attachment);
            }
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


        System.out.println("test");
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

    public ResponseEntity<?> addEmployeeToEvent(int employeeId, int eventId) {
        return null;
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

    public ResponseEntity<Person> addPersonToEvent(int PersonId, int EventId) { //void?
        return null;
    }

    public ResponseEntity<Person> removePersonFromEvent(int PersonId, int EventId) { //void?
        return null;
    }
}
