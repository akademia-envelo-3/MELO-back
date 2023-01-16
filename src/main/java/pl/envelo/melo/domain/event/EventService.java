package pl.envelo.melo.domain.event;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.envelo.melo.authorization.employee.Employee;
import pl.envelo.melo.authorization.employee.EmployeeRepository;
import pl.envelo.melo.authorization.employee.dto.EmployeeDto;
import pl.envelo.melo.authorization.person.Person;
import pl.envelo.melo.authorization.person.PersonRepository;
import pl.envelo.melo.domain.attachment.AttachmentRepository;
import pl.envelo.melo.domain.category.CategoryRepository;
import pl.envelo.melo.domain.comment.CommentRepository;
import pl.envelo.melo.domain.event.dto.EventDetailsDto;
import pl.envelo.melo.domain.event.dto.EventToDisplayOnListDto;
import pl.envelo.melo.domain.event.dto.NewEventDto;
import pl.envelo.melo.domain.hashtag.HashtagRepository;
import pl.envelo.melo.domain.location.LocationRepository;
import pl.envelo.melo.domain.poll.PollAnswerRepository;
import pl.envelo.melo.domain.poll.PollRepository;
import pl.envelo.melo.domain.poll.PollTemplateRepository;
import pl.envelo.melo.mappers.EventMapper;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class EventService {

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
    private EventMapper eventMapper;


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

    public ResponseEntity<Event> updateEvent(int id, NewEventDto newEventDto) { //void?
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
