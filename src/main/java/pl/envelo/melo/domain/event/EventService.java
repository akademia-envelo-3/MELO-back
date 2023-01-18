package pl.envelo.melo.domain.event;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.envelo.melo.authorization.employee.Employee;
import pl.envelo.melo.authorization.employee.EmployeeRepository;
import pl.envelo.melo.authorization.employee.dto.EmployeeDto;
import pl.envelo.melo.authorization.person.Person;
import pl.envelo.melo.domain.attachment.AttachmentRepository;
import pl.envelo.melo.domain.attachment.Attachment;
import pl.envelo.melo.domain.category.CategoryRepository;
import pl.envelo.melo.domain.event.dto.EventDetailsDto;
import pl.envelo.melo.domain.event.dto.EventToDisplayOnListDto;
import pl.envelo.melo.domain.event.dto.NewEventDto;
import pl.envelo.melo.domain.hashtag.Hashtag;
import pl.envelo.melo.domain.hashtag.HashtagRepository;
import pl.envelo.melo.domain.location.LocationRepository;
import pl.envelo.melo.domain.unit.UnitRepository;
import pl.envelo.melo.domain.poll.PollAnswerRepository;
import pl.envelo.melo.domain.poll.PollRepository;
import pl.envelo.melo.domain.poll.PollTemplateRepository;
import pl.envelo.melo.mappers.EventDetailsMapper;
import pl.envelo.melo.mappers.EventMapper;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class EventService {

    @Autowired
    private EventDetailsMapper eventDetailsMapper;
    private final EventRepository eventRepository;
    private final EmployeeRepository employeeRepository;
    private final HashtagRepository hashtagRepository;
    private final CategoryRepository categoryRepository;
    private final AttachmentRepository attachmentRepository;
    private final LocationRepository locationRepository;
    private final UnitRepository unitRepository;
    private final EventMapper eventMapper;
    private final PollTemplateRepository pollTemplateRepository;
    private final PollRepository pollRepository;
    private final PollAnswerRepository pollAnswerRepository;
    private final CommentRepository commentRepository;
    private final PersonRepository personRepository;
    private EventMapper eventMapper;

    public ResponseEntity<?> getEvent(int id) {
        if (eventRepository.existsById(id)) {
            Event event = eventRepository.findById(id).get();
            return ResponseEntity.ok(eventDetailsMapper.convert(event));
        } else {

            return ResponseEntity.status(404).body("Event with this ID do not exist");
        }

    public ResponseEntity<EventDetailsDto> getEvent(int id) {
//        return eventRepository.findById(id);
        return null;
    }

    public ResponseEntity<List<EventToDisplayOnListDto>> listAllEvents() {
        List<Event> result = eventRepository.findAllByStartTimeAfterAndType(LocalDateTime.now(), EventType.LIMITED_EXTERNAL);
        result.addAll(eventRepository.findAllByStartTimeAfterAndType(LocalDateTime.now(), EventType.UNLIMITED_EXTERNAL));
        result.addAll(eventRepository.findAllByStartTimeAfterAndType(LocalDateTime.now(), EventType.UNLIMITED_PUBLIC_INTERNAL));
        result.addAll(eventRepository.findAllByStartTimeAfterAndType(LocalDateTime.now(), EventType.LIMITED_PUBLIC_INTERNAL));
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

        locationRepository.save(event.getLocation());
        //todo swap with locationService method when present

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
