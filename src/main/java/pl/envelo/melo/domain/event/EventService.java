package pl.envelo.melo.domain.event;

import lombok.AllArgsConstructor;
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
import pl.envelo.melo.domain.attachment.dto.AttachmentDto;
import pl.envelo.melo.domain.category.CategoryRepository;
import pl.envelo.melo.domain.comment.CommentRepository;
import pl.envelo.melo.domain.event.dto.EventDetailsDto;
import pl.envelo.melo.domain.event.dto.EventToDisplayOnListDto;
import pl.envelo.melo.domain.event.dto.NewEventDto;
import pl.envelo.melo.domain.hashtag.Hashtag;
import pl.envelo.melo.domain.hashtag.HashtagDto;
import pl.envelo.melo.domain.hashtag.HashtagRepository;
import pl.envelo.melo.domain.hashtag.HashtagService;
import pl.envelo.melo.domain.location.LocationRepository;
import pl.envelo.melo.domain.notification.NotificationService;
import pl.envelo.melo.domain.poll.PollAnswerRepository;
import pl.envelo.melo.domain.poll.PollRepository;
import pl.envelo.melo.domain.poll.PollTemplateRepository;
import pl.envelo.melo.domain.unit.Unit;
import pl.envelo.melo.domain.unit.UnitRepository;
import pl.envelo.melo.mappers.AttachmentMapper;
import pl.envelo.melo.mappers.EventEditMapper;
import pl.envelo.melo.mappers.EventMapper;
import pl.envelo.melo.mappers.HashtagMapper;
import pl.envelo.melo.validators.EventValidator;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class EventService {

    private final HashtagService hashtagService;
    private final NotificationService notificationService;
    private final EventRepository eventRepository;
    private final EmployeeRepository employeeRepository;
    private final HashtagRepository hashtagRepository;
    private final CategoryRepository categoryRepository;
    private final AttachmentRepository attachmentRepository;
    private final LocationRepository locationRepository;
    private final PollTemplateRepository pollTemplateRepository;
    private final PollRepository pollRepository;
    private final PollAnswerRepository pollAnswerRepository;
    private final CommentRepository commentRepository;
    private final PersonRepository personRepository;
    private final UnitRepository unitRepository;
    private EventMapper eventMapper;
    private HashtagMapper hashtagMapper;
    private EventEditMapper eventEditMapper;
    private AttachmentMapper attachmentMapper;
    private EventUpdater eventUpdater;
    private EventValidator eventValidator;
    private EditEventNotificationHandler eventNotificationHandler;

    public ResponseEntity<EventDetailsDto> getEvent(int id) {
//        return eventRepository.findById(id); Mapper dodać
        return null;
    }

    public ResponseEntity<List<EventToDisplayOnListDto>> listAllEvents() {
        List<Event> result = eventRepository.findAllByStartTimeAfterAndType(LocalDateTime.now(), EventType.LIMITED_EXTERNAL);
        result.addAll(eventRepository.findAllByStartTimeAfterAndType(LocalDateTime.now(), EventType.UNLIMITED_EXTERNAL));
        result.addAll(eventRepository.findAllByStartTimeAfterAndType(LocalDateTime.now(), EventType.UNLIMITED_PUBLIC_INTERNAL));
        result.addAll(eventRepository.findAllByStartTimeAfterAndType(LocalDateTime.now(), EventType.LIMITED_PUBLIC_INTERNAL));
        return ResponseEntity.ok(result.stream().map(eventMapper::convert).toList());
    }

    public ResponseEntity<Event> insertNewEvent(NewEventDto newEventDto) {  //void?
        return null;
    }

    public ResponseEntity<EmployeeDto> getEventOrganizer(int id) {
        return null;
    }

    public ResponseEntity<Employee> changeEventOrganizer(int id, Employee employee) { //void?
        return null;
    }

    public ResponseEntity<?> updateEvent(int id, NewEventDto newEventDto) { //void?
        //TODO dostosować do funckjonalnosci wysyłania plików na serwer
        try {
            Optional<Event> optionalEvent = eventRepository.findById(id);
            if (optionalEvent.isEmpty())
                throw new RuntimeException();
            Event event = optionalEvent.get();

            Map<String, String> validationResult = eventValidator.validateToEdit(event, newEventDto);
            validationResult.forEach((k, v) -> System.out.println(k + " " + v));
            if(validationResult.size()!=0){
                return ResponseEntity.status(HttpStatusCode.valueOf(500)).body(validationResult);
            }
            eventUpdater.update(event, newEventDto);
            eventNotificationHandler.editNotification(event, newEventDto).forEach(notificationService::insertEventNotification);
            eventRepository.save(event);
            //}else{
            //    return ResponseEntity.status(HttpStatusCode.valueOf(403)).body(validationResult);
            //}
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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
