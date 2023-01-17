package pl.envelo.melo.domain.event;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.envelo.melo.authorization.employee.Employee;
import pl.envelo.melo.authorization.employee.EmployeeRepository;
import pl.envelo.melo.authorization.employee.EmployeeService;
import pl.envelo.melo.authorization.employee.dto.EmployeeDto;
import pl.envelo.melo.authorization.person.Person;
import pl.envelo.melo.authorization.person.PersonRepository;
import pl.envelo.melo.domain.attachment.AttachmentRepository;
import pl.envelo.melo.domain.attachment.Attachment;
import pl.envelo.melo.domain.attachment.dto.AttachmentDto;
import pl.envelo.melo.domain.category.CategoryRepository;
import pl.envelo.melo.domain.comment.CommentRepository;
import pl.envelo.melo.domain.event.dto.EventDetailsDto;
import pl.envelo.melo.domain.event.dto.EventToDisplayOnListDto;
import pl.envelo.melo.domain.event.dto.NewEventDto;
import pl.envelo.melo.domain.hashtag.Hashtag;
import pl.envelo.melo.domain.hashtag.HashtagRepository;
import pl.envelo.melo.domain.location.LocationRepository;
import pl.envelo.melo.domain.poll.Poll;
import pl.envelo.melo.domain.poll.PollAnswerRepository;
import pl.envelo.melo.domain.poll.PollRepository;
import pl.envelo.melo.domain.poll.PollTemplateRepository;
import pl.envelo.melo.domain.unit.Unit;
import pl.envelo.melo.domain.unit.UnitRepository;
import pl.envelo.melo.mappers.AttachmentMapper;
import pl.envelo.melo.mappers.EmployeeMapper;
import pl.envelo.melo.mappers.EventMapper;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final EmployeeRepository employeeRepository;
    private final HashtagRepository hashtagRepository;
    private final CategoryRepository categoryRepository;
    private final AttachmentRepository attachmentRepository;
    private final LocationRepository locationRepository;
    private final UnitRepository unitRepository;
    private final EventMapper eventMapper;



    public ResponseEntity<EventDetailsDto> getEvent(int id) {
//        return eventRepository.findById(id);
        return null;
    }

    public ResponseEntity<List<EventToDisplayOnListDto>> listAllEvents() {
        return null;
    }

    //    @Transactional
    public ResponseEntity<?> insertNewEvent(NewEventDto newEventDto) {  //void?
//members i comments +17
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
                event.setCategory(null);
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
