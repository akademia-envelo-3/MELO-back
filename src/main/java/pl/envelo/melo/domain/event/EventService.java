package pl.envelo.melo.domain.event;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.envelo.melo.authorization.employee.Employee;
import pl.envelo.melo.authorization.employee.EmployeeRepository;
import pl.envelo.melo.authorization.employee.dto.EmployeeDto;
import pl.envelo.melo.authorization.person.Person;
import pl.envelo.melo.authorization.person.PersonRepository;
import pl.envelo.melo.domain.attachment.Attachment;
import pl.envelo.melo.domain.attachment.AttachmentRepository;
import pl.envelo.melo.domain.category.CategoryRepository;
import pl.envelo.melo.domain.comment.CommentRepository;
import pl.envelo.melo.domain.event.dto.EventToDisplayOnListDto;
import pl.envelo.melo.domain.event.dto.NewEventDto;
import pl.envelo.melo.domain.hashtag.Hashtag;
import pl.envelo.melo.domain.hashtag.HashtagRepository;
import pl.envelo.melo.domain.hashtag.HashtagService;
import pl.envelo.melo.domain.location.LocationRepository;
import pl.envelo.melo.domain.location.LocationService;
import pl.envelo.melo.domain.notification.NotificationService;
import pl.envelo.melo.domain.unit.UnitRepository;
import pl.envelo.melo.domain.poll.PollAnswerRepository;
import pl.envelo.melo.domain.poll.PollRepository;
import pl.envelo.melo.domain.poll.PollTemplateRepository;

import pl.envelo.melo.mappers.*;
import pl.envelo.melo.validators.EventValidator;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
public class EventService {

    private final HashtagService hashtagService;
    private final NotificationService notificationService;
    private EventDetailsMapper eventDetailsMapper;
    private final EventRepository eventRepository;
    private final EmployeeRepository employeeRepository;
    private final HashtagRepository hashtagRepository;
    private final CategoryRepository categoryRepository;
    private final AttachmentRepository attachmentRepository;
    private final LocationRepository locationRepository;
    private final LocationService locationService;
    private final UnitRepository unitRepository;
    private final PollTemplateRepository pollTemplateRepository;
    private final PollRepository pollRepository;
    private final PollAnswerRepository pollAnswerRepository;
    private final CommentRepository commentRepository;
    private final PersonRepository personRepository;
    private EventMapper eventMapper;
    private HashtagMapper hashtagMapper;
    private EventEditMapper eventEditMapper;
    private AttachmentMapper attachmentMapper;
    private EventUpdater eventUpdater;
    private EventValidator eventValidator;
    private EditEventNotificationHandler eventNotificationHandler;

    public ResponseEntity<?> getEvent(int id) {
        if (eventRepository.existsById(id)) {
            Event event = eventRepository.findById(id).get();
            return ResponseEntity.ok(eventDetailsMapper.convert(event));
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
        if(event.getType().toString().startsWith("LIMITED")) {
            if(event.getMemberLimit()<1) {
                return ResponseEntity.status(400).body("Event with limited eventType must have higher memberLimit than 0.");
            }
        }

        if (newEventDto.getLocation() != null) {
            event.setLocation(locationService.insertOrGetLocation(newEventDto.getLocation()));
        }

        if(employeeRepository.existsById(newEventDto.getOrganizerId())) {
            event.setOrganizer(employeeRepository.findById(newEventDto.getOrganizerId()).get());
        }

        if (!(event.getMainPhoto() == null)) {
            attachmentRepository.save(event.getMainPhoto());
        } else {
            event.setMainPhoto(null); //todo swap with attachmentMainPhoto method
        }


        if (!(newEventDto.getCategoryId() == null)) {
            if(categoryRepository.findById(newEventDto.getCategoryId()).isPresent()) {
                event.setCategory(categoryRepository.findById(newEventDto.getCategoryId()).get());
            }
            else {
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
            if(unitRepository.findById(newEventDto.getUnitId()).isPresent()) {
                event.setUnit(unitRepository.findById(newEventDto.getUnitId()).get());
            }
            else {
                event.setUnit(null);
            }
        } //todo create UnitMapper and use UnitRepository to find unit in database


        System.out.println("test");
        return new ResponseEntity(eventRepository.save(event), HttpStatus.CREATED);
    }

    public ResponseEntity<EmployeeDto> getEventOrganizer(int id) {
        return null;
    }

    public ResponseEntity<Employee> changeEventOrganizer(int id, Employee employee) { //void?
        return null;
    }

    public ResponseEntity<?> updateEvent(int id, NewEventDto newEventDto) { //void?
        //TODO dostosować do funckjonalnosci wysyłania plików na serwer
            Optional<Event> optionalEvent = eventRepository.findById(id);
            if (optionalEvent.isEmpty())
                return ResponseEntity.badRequest().body("Event with id " +id+" not found");
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
    public ResponseEntity<?> editEventForm(int id){
        if(!eventRepository.existsById(id))
            return ResponseEntity.status(HttpStatusCode.valueOf(404)).body("Event with "+id+" does not exists");
        return ResponseEntity.ok(eventEditMapper.convert(eventRepository.getReferenceById(id)));
    }
    public ResponseEntity<Employee> addEmployeeToEvent(int EmployeeId, int EventId) { //void?
        return null;
    }

    public ResponseEntity<Employee> removeEmployeeFromEvent(int EmployeeId, int EventId) { //void?
        return null;
    }

    public ResponseEntity<Person> addPersonToEvent(int PersonId, int EventId) { //void?
        return null;
    }

    public ResponseEntity<Person> removePersonFromEvent(int PersonId, int EventId) { //void?
        return null;
    }
}
