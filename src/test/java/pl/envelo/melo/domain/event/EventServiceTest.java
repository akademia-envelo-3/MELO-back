package pl.envelo.melo.domain.event;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import pl.envelo.melo.authorization.employee.Employee;
import pl.envelo.melo.authorization.employee.EmployeeRepository;
import pl.envelo.melo.authorization.person.Person;
import pl.envelo.melo.authorization.person.PersonRepository;
import pl.envelo.melo.authorization.user.UserRepository;
import pl.envelo.melo.domain.attachment.Attachment;
import pl.envelo.melo.domain.attachment.AttachmentType;
import pl.envelo.melo.domain.attachment.dto.AttachmentDto;
import pl.envelo.melo.domain.event.dto.EventToDisplayOnListDto;
import pl.envelo.melo.domain.event.dto.NewEventDto;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class EventServiceTest {
    @Autowired
    EventService eventService;
    @Autowired
    EventRepository eventRepository;
    @Autowired
    EmployeeRepository employeeRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    PersonRepository personRepository;

    SimpleEventMocker simpleEventMocker;

    @BeforeEach
    void setUp() {
        eventRepository.deleteAll();
        eventRepository.flush();
        simpleEventMocker = new SimpleEventMocker(employeeRepository, eventRepository, personRepository, userRepository);
    }

    @Test
    void listAllEvents() {
        Event presentEvent = simpleEventMocker.mockEvent(LocalDateTime.now().plusDays(5), EventType.LIMITED_PUBLIC_INTERNAL, simpleEventMocker.mockEmployee("test"), simpleEventMocker.mockEmployee("test2"));
        Event presentPrivateEvent = simpleEventMocker.mockEvent(LocalDateTime.now().plusDays(5), EventType.LIMITED_PRIVATE_INTERNAL, simpleEventMocker.mockEmployee("test"), simpleEventMocker.mockEmployee("test2"));
        Event pastEvent = simpleEventMocker.mockEvent(LocalDateTime.now().minusDays(5), EventType.UNLIMITED_PUBLIC_INTERNAL);
        EventToDisplayOnListDto eventToDisplayOnListDto = eventService.listAllEvents().getBody().get(0);
        assertEquals(2, eventToDisplayOnListDto.getInvitedMembersNumber());
        assertEquals(presentEvent.getId(), eventToDisplayOnListDto.getEventId());
        assertEquals(1, eventService.listAllEvents().getBody().size());
        assertNull(eventToDisplayOnListDto.getMainPhoto());
    }

    @Test
    void updateEvent() {
        Event event = simpleEventMocker.mockEvent(LocalDateTime.now().plusDays(5), EventType.LIMITED_PUBLIC_INTERNAL);
        event.setAttachments(new HashSet<>());
        Attachment attachment = new Attachment();
        attachment.setAttachmentType(AttachmentType.PHOTO);
        attachment.setAttachmentUrl("http://wp.pl/photo");
        event.getAttachments().add(attachment);
        eventRepository.save(event);

        Employee employee = simpleEventMocker.mockEmployee("owwneer");

        NewEventDto newEventDto = new NewEventDto();
        newEventDto.setEventType(EventType.LIMITED_PUBLIC_INTERNAL);
        newEventDto.setOrganizerId(employee.getId());
        newEventDto.setName("Ddds");
        newEventDto.setDescription("dsadsa");
        newEventDto.setPeriodicType(PeriodicType.NONE);
        newEventDto.setStartTime(event.getStartTime());
        newEventDto.setEndTime(event.getEndTime());

        Employee invited = simpleEventMocker.mockEmployee("juh");
        Employee invited2 = simpleEventMocker.mockEmployee("juh2");
        employeeRepository.save(invited);
        employeeRepository.save(invited2);
        newEventDto.setInvitedMembers(new HashSet<>());
        newEventDto.getInvitedMembers().add(invited.getId());
        newEventDto.getInvitedMembers().add(invited2.getId());
        assertTrue(((ResponseEntity<Map>) (eventService.updateEvent(event.getId(), newEventDto))).getBody().containsKey("memberLimit" + " error"));
        newEventDto.setMemberLimit(444);
        eventService.updateEvent(event.getId(), newEventDto);
        event = eventRepository.getReferenceById(event.getId());

        assertEquals(employee.getUser().getPerson().getFirstName(), event.getOrganizer().getUser().getPerson().getFirstName());
        assertEquals("juh", event.getInvited().stream().filter(e -> e.getId() == invited.getId()).findFirst().get().getUser().getPerson().getFirstName());
        assertEquals(2, event.getInvited().size());
        AttachmentDto attachmentDto = new AttachmentDto();
        attachmentDto.setAttachmentType(AttachmentType.PHOTO);
        attachmentDto.setAttachmentUrl("http://wp.pl/photo");
        newEventDto.setAttachments(new HashSet<>());
        newEventDto.getAttachments().add(attachmentDto);

        eventService.updateEvent(event.getId(), newEventDto);

        //TODO sprawdzić attachment po implementacji multipart file upload
        event = eventRepository.getReferenceById(event.getId());
        assertEquals(attachmentDto.getAttachmentUrl(), event.getAttachments().stream().findFirst().get().getAttachmentUrl());
        assertEquals(1, event.getAttachments().size());
        //TODO sprawdzić notification box
        //TODO sprawdzić hashtagi
    }
}