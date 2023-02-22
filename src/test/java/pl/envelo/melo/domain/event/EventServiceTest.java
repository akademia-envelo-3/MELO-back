package pl.envelo.melo.domain.event;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pl.envelo.melo.EventContextTest;
import pl.envelo.melo.authorization.employee.Employee;
import pl.envelo.melo.domain.event.dto.EventDetailsDto;
import pl.envelo.melo.domain.event.dto.EventToDisplayOnListDto;
import pl.envelo.melo.domain.event.dto.NewEventDto;
import pl.envelo.melo.mappers.EventDetailsMapper;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class EventServiceTest extends EventContextTest {


    @Autowired
    EventDetailsMapper eventDetailsMapper;
    @Autowired
    EventService eventService;

    @Test
    void getExistEvent() {

        Event event = simpleEventMocker.mockEvent(LocalDateTime.now(), EventType.LIMITED_EXTERNAL);
        event.setMemberLimit(10L);
        EventDetailsDto eventDetailsDto = eventDetailsMapper.convert(event);
        ResponseEntity<?> eventDetailsDtoResponseEntity = eventService.getEvent(1, 1);

        assertEquals(HttpStatus.OK, eventDetailsDtoResponseEntity.getStatusCode());
        assertEquals(event.getName(), eventDetailsDto.getName());
        assertEquals(event.getDescription(), eventDetailsDto.getDescription());
        assertEquals(event.getOrganizer().getUser().getPerson().getFirstName()
                , eventDetailsDto.getOrganizer().getFirstName());
        assertEquals(event.getMemberLimit(), eventDetailsDto.getMemberLimit());

        Optional<Employee> organizer = employeeRepository.findById(event.getOrganizer().getId());
        assertEquals(organizer.get().getUser().getPerson().getFirstName(), eventDetailsDto.getOrganizer().getFirstName());
    }

    @Test
    void checkNonExistentEvent() {
        ResponseEntity<?> eventDetailsDtoResponseEntity = eventService.getEvent(3, 1);
        assertEquals(HttpStatus.NOT_FOUND, eventDetailsDtoResponseEntity.getStatusCode());
    }

    @Test
    void listAllEvents() {
        Event presentEvent = simpleEventMocker.mockEvent(LocalDateTime.now().plusDays(5), EventType.LIMITED_PUBLIC_INTERNAL, simpleEventMocker.mockEmployee("test"), simpleEventMocker.mockEmployee("test2"));
        Event presentBeforeEvent = simpleEventMocker.mockEvent(LocalDateTime.now().plusDays(2), EventType.LIMITED_PUBLIC_INTERNAL, simpleEventMocker.mockEmployee("test"), simpleEventMocker.mockEmployee("test2"), simpleEventMocker.mockEmployee("test3"));
        Event presentPrivateEvent = simpleEventMocker.mockEvent(LocalDateTime.now().plusDays(5), EventType.LIMITED_PRIVATE_INTERNAL, simpleEventMocker.mockEmployee("test"), simpleEventMocker.mockEmployee("test2"));
        Event pastEvent = simpleEventMocker.mockEvent(LocalDateTime.now().minusDays(5), EventType.UNLIMITED_PUBLIC_INTERNAL);
        EventToDisplayOnListDto eventToDisplayOnListDto = Objects.requireNonNull(eventService.listAllEvents().getBody()).get(0);
        assertEquals(3, eventToDisplayOnListDto.getInvitedMembersNumber());
        eventToDisplayOnListDto = Objects.requireNonNull(eventService.listAllEvents().getBody()).get(1);
        assertEquals(2, eventToDisplayOnListDto.getInvitedMembersNumber());
        assertEquals(presentEvent.getId(), eventToDisplayOnListDto.getEventId());
        assertEquals(2, Objects.requireNonNull(eventService.listAllEvents().getBody()).size());

        assertNull(eventToDisplayOnListDto.getMainPhoto());
    }

    @Test
    void updateEvent() {
//        Event event = simpleEventMocker.mockEvent(LocalDateTime.now().plusDays(5), EventType.LIMITED_PUBLIC_INTERNAL);
//        eventRepository.save(event);
//
//        Employee employee = simpleEventMocker.mockEmployee("owwneer");
//
//        NewEventDto newEventDto = new NewEventDto();
//        newEventDto.setEventType(EventType.LIMITED_PUBLIC_INTERNAL);
//        newEventDto.setOrganizerId(employee.getId());
//        newEventDto.setName("Ddds");
//        newEventDto.setDescription("dsadsa");
//        newEventDto.setPeriodicType(PeriodicType.NONE);
//        newEventDto.setStartTime(event.getStartTime());
//        newEventDto.setEndTime(event.getEndTime());
//
//        Employee invited = simpleEventMocker.mockEmployee("juh");
//        Employee invited2 = simpleEventMocker.mockEmployee("juh2");
//        employeeRepository.save(invited);
//        employeeRepository.save(invited2);
//        newEventDto.setInvitedMembers(new HashSet<>());
//        newEventDto.getInvitedMembers().add(invited.getId());
//        newEventDto.getInvitedMembers().add(invited2.getId());
//        assertTrue(((ResponseEntity<Map>) (eventService.updateEvent(event.getId(), newEventDto))).getBody().containsKey("memberLimit" + " error"));
//        newEventDto.setMemberLimit(444);
//        eventService.updateEvent(event.getId(), newEventDto);
//        event = eventRepository.getReferenceById(event.getId());
//
//        assertEquals(employee.getUser().getPerson().getFirstName(), event.getOrganizer().getUser().getPerson().getFirstName());
//        assertEquals("juh", event.getInvited().stream().filter(e -> e.getId() == invited.getId()).findFirst().get().getUser().getPerson().getFirstName());
//        assertEquals(2, event.getInvited().size());
//
//
//        eventService.updateEvent(event.getId(), newEventDto);
//
//        //TODO sprawdzić attachment po implementacji multipart file upload
//        event = eventRepository.getReferenceById(event.getId());
//        //TODO sprawdzić notification box
//        //TODO sprawdzić hashtagi
    }

    @Test
    void editEventForm() {
        Event event = simpleEventMocker.mockEvent(LocalDateTime.now().plusDays(5), EventType.LIMITED_PUBLIC_INTERNAL);
        eventRepository.save(event);
        event.setPeriodicType(PeriodicType.NONE);
        assertEquals(PeriodicType.NONE, ((NewEventDto) Objects.requireNonNull(eventService.editEventForm(event.getId()).getBody())).getPeriodicType());
    }

    @Test
    void removeEmployeeFromEventTest() {
        Event event = simpleEventMocker.mockEvent(LocalDateTime.now().plusDays(5),
                EventType.LIMITED_PUBLIC_INTERNAL);
        Employee test = simpleEventMocker.mockEmployee("test");
        event.getMembers().add(test.getUser().getPerson());

        Set<Event> eventSet = new HashSet<>();
        eventSet.add(event);
        test.setJoinedEvents(eventSet);

        assertFalse(event.getMembers().isEmpty());
        assertTrue(event.getMembers().contains(test.getUser().getPerson()));
        assertTrue(test.getJoinedEvents().contains(event));

        eventService.removeEmployeeFromEvent(test.getId(), event.getId());

        assertTrue(test.getJoinedEvents().isEmpty());
        assertFalse(event.getMembers().contains(test.getUser().getPerson()));

    }

    @Test
    void removeEmployeeFromEventThrowExceptionTest() {
        Employee test = simpleEventMocker.mockEmployee("test");
        Event event = simpleEventMocker.mockEvent(LocalDateTime.now().plusDays(5),
                EventType.LIMITED_PUBLIC_INTERNAL, test);

        Set<Event> eventSet = new HashSet<>();
        eventSet.add(event);
        test.setJoinedEvents(eventSet);

        assertFalse(event.getMembers().isEmpty());
        assertTrue(event.getMembers().contains(test.getUser().getPerson()));
        assertTrue(test.getJoinedEvents().contains(event));
        ResponseEntity responseEntity = eventService.removeEmployeeFromEvent(test.getId(), event.getId());

        assertEquals(ResponseEntity.status(403).body("Event organizer cant be remove from his event"),
                responseEntity);

        assertFalse(test.getJoinedEvents().isEmpty());
        assertTrue(event.getMembers().contains(test.getUser().getPerson()));

    }

    @Test
    void changeOrganizerTest() {
        Employee test = simpleEventMocker.mockEmployee("test");
        Employee test2 = simpleEventMocker.mockEmployee("test2");
        Event event = simpleEventMocker.mockEvent(LocalDateTime.now().plusDays(5),
                EventType.UNLIMITED_PUBLIC_INTERNAL, test, test2);

        Set<Event> eventSet = new HashSet<>();
        eventSet.add(event);
        test.setOwnedEvents(eventSet);

        assertTrue(test.getOwnedEvents().contains(event));
        assertEquals(test.getId(), event.getOrganizer().getId());

        eventService.changeEventOrganizer(event.getId(), test2.getId());

        assertFalse(test.getOwnedEvents().contains(event));
        assertNotEquals(test.getId(), event.getOrganizer().getId());

        assertTrue(test2.getOwnedEvents().contains(event));
        assertEquals(test2.getId(), event.getOrganizer().getId());


    }

    @Test
    void addEmployeeToEvent() {
        Employee test = simpleEventMocker.mockEmployee("test");
        Employee member = simpleEventMocker.mockEmployee("member");
        Event event = simpleEventMocker.mockEvent(LocalDateTime.now().plusDays(5),
                EventType.LIMITED_PUBLIC_INTERNAL, test);
        event.setMemberLimit(3L);
        eventRepository.save(event);
        assertEquals(1, event.getMembers().size());
        ResponseEntity<?> addedMember = eventService.addEmployeeToEvent(member.getId(), event.getId());
        assertEquals(HttpStatus.OK, addedMember.getStatusCode());
        assertEquals(2, event.getMembers().size());
        addedMember = eventService.addEmployeeToEvent(member.getId(), event.getId());
        assertEquals(HttpStatus.valueOf(400), addedMember.getStatusCode());
        assertEquals("Employee already on list", addedMember.getBody());
        Employee member1 = simpleEventMocker.mockEmployee("member1");
        addedMember = eventService.addEmployeeToEvent(member1.getId(), event.getId());
        addedMember = eventService.addEmployeeToEvent(member1.getId(), event.getId());
        assertEquals("Event is full", addedMember.getBody());
    }
}