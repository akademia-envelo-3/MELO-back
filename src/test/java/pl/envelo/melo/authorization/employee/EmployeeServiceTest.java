package pl.envelo.melo.authorization.employee;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pl.envelo.melo.EventContextTest;
import pl.envelo.melo.domain.event.Event;
import pl.envelo.melo.domain.event.EventType;
import pl.envelo.melo.domain.event.dto.EventToDisplayOnListDto;
import pl.envelo.melo.domain.unit.Unit;
import pl.envelo.melo.domain.unit.UnitRepository;
import pl.envelo.melo.domain.unit.UnitService;
import pl.envelo.melo.domain.unit.dto.UnitToDisplayOnListDto;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
class EmployeeServiceTest extends EventContextTest {
    @Autowired
    EmployeeService employeeService;
    @Autowired
    private UnitRepository unitRepository;
    @Autowired
    private UnitService unitService;
    @Test
    void getSetOfOwnedEvents() {
        //Testy
        ResponseEntity<?> responseEntity = employeeService.getSetOfOwnedEvents(1);
        assertEquals(HttpStatus.NOT_FOUND, employeeService.getSetOfOwnedEvents(1).getStatusCode());
        Event event = simpleEventMocker.mockEvent(LocalDateTime.now().plusDays(5),EventType.LIMITED_PUBLIC_INTERNAL);
        event.setName("Test name");
        event.getOrganizer().setOwnedEvents(new HashSet<>());
        event.getOrganizer().setJoinedEvents(new HashSet<>());
        event.getOrganizer().getOwnedEvents().add(event);
        event.getOrganizer().getJoinedEvents().add(event);
        System.out.println(event.getOrganizer().getId());
        responseEntity = employeeService.getSetOfOwnedEvents(event.getOrganizer().getId());
        Set<EventToDisplayOnListDto> events = (Set<EventToDisplayOnListDto>) responseEntity.getBody();
        assertEquals(1, events.size());//Test name
        assertEquals("Test name", events.stream().findFirst().get().getName());
        assertEquals(HttpStatus.valueOf(404), employeeService.getSetOfOwnedEvents(2).getStatusCode());


    }

    @Test
    void addToOwnedEvents() {
        Employee employee = simpleEventMocker.mockEmployee("test");
        Event event1 = simpleEventMocker.mockEvent(LocalDateTime.now().plusMonths(1), EventType.UNLIMITED_EXTERNAL, employee);
        Event event = simpleEventMocker.mockEvent(LocalDateTime.now().plusMonths(1).plusDays(2), EventType.UNLIMITED_EXTERNAL, employee);
        employeeService.addToOwnedEvents(employee.getId(), event1);
        int len = employeeService.getEmployee(employee.getId()).getBody().getOwnedEvents().size();
        employeeService.addToOwnedEvents(employee.getId(), event);
        assertEquals(len + 1, employeeService.getEmployee(employee.getId()).getBody().getOwnedEvents().size());

    }

    @Test
    void removeFromOwnedEvents() {
        //Dane
        Employee employee = simpleEventMocker.mockEmployee("test");
        Event event1 = simpleEventMocker.mockEvent(LocalDateTime.now().plusMonths(1), EventType.UNLIMITED_EXTERNAL, employee);
        Event event = simpleEventMocker.mockEvent(LocalDateTime.now().plusMonths(1).plusDays(2), EventType.UNLIMITED_EXTERNAL, employee);
        employeeService.addToOwnedEvents(employee.getId(), event);
        employeeService.addToOwnedEvents(employee.getId(), event1);
        //Test
        int len = employeeService.getEmployee(employee.getId()).getBody().getOwnedEvents().size();
        employeeService.removeFromOwnedEvents(employee.getId(), event1);
        assertEquals(len - 1, employeeService.getEmployee(employee.getId()).getBody().getOwnedEvents().size());
    }

    @Test
    void addToJoinedEvents() {
        //Dane
        Employee employee = simpleEventMocker.mockEmployee("test");
        Event event1 = simpleEventMocker.mockEvent(LocalDateTime.now().plusMonths(1), EventType.UNLIMITED_EXTERNAL, employee);
        Event event = simpleEventMocker.mockEvent(LocalDateTime.now().plusMonths(1).plusDays(2), EventType.UNLIMITED_EXTERNAL, employee);
        //Test
        employeeService.addToJoinedEvents(employee.getId(), event1);
        int len = employee.getJoinedEvents().size();
        employeeService.addToJoinedEvents(employee.getId(), event);
        assertEquals(len + 1, employee.getJoinedEvents().size());
    }


    @Test
    void removeFromJoinedEvents() {
        //Dane
        Employee employee = simpleEventMocker.mockEmployee("test");
        Employee employee1 = simpleEventMocker.mockEmployee("test");
        Event event1 = simpleEventMocker.mockEvent(LocalDateTime.now().plusMonths(1), EventType.UNLIMITED_EXTERNAL, employee);
        Event event = simpleEventMocker.mockEvent(LocalDateTime.now().plusMonths(1).plusDays(2), EventType.UNLIMITED_EXTERNAL, employee);
        //Test
        employeeService.addToJoinedEvents(employee.getId(), event);
        employeeService.addToJoinedEvents(employee1.getId(), event);
        employeeService.addToJoinedEvents(employee.getId(), event1);
        int len = employee1.getJoinedEvents().size();
        assertTrue(employeeService.removeFromJoinedEvents(employee1.getId(), event));
        assertEquals(len - 1, employee1.getJoinedEvents().size());
        assertFalse(employeeService.removeFromJoinedEvents(employee.getId(),event));
    }

    @Test
    void addToOwnedUnits() {
        //Dane
        Employee employee = simpleEventMocker.mockEmployee("test");
        int len;
        Unit unit = new Unit();
        unit.setId(1);
        unit.setName("unit1");
        unit.setOwner(employee);
        unitRepository.save(unit);
        Unit unit2 = new Unit();
        unit2.setId(2);
        unit2.setName("unit2");
        unit2.setOwner(employee);
        unitRepository.save(unit2);
        //Test
        employeeService.addToOwnedUnits(employee.getId(), unit);
        len = employee.getOwnedUnits().size();
        employeeService.addToOwnedUnits(employee.getId(), unit2);
        assertEquals(len + 1, employee.getOwnedUnits().size());
    }

    @Test
    void removeFromOwnedUnits() {
        //Dane
        Employee employee = simpleEventMocker.mockEmployee("test");
        int len;
        Unit unit = new Unit();
        unit.setId(1);
        unit.setName("unit1");
        unit.setOwner(employee);
        unitRepository.save(unit);
        Unit unit2 = new Unit();
        unit2.setId(2);
        unit2.setName("unit2");
        unit2.setOwner(employee);
        unitRepository.save(unit2);
        employeeService.addToOwnedUnits(employee.getId(), unit);
        employeeService.addToOwnedUnits(employee.getId(), unit2);
        //Test
        len = employee.getOwnedUnits().size();
        assertTrue(employeeService.removeFromOwnedUnits(unit.getOwner().getId(),unit));
        assertEquals(len - 1, employee.getOwnedUnits().size());
    }

    @Test
    void addToJoinedUnits() {
        //Dane
        Employee employee = simpleEventMocker.mockEmployee("test");
        int len;
        Unit unit = new Unit();
        unit.setId(1);
        unit.setName("unit1");
        unit.setOwner(employee);
        unitRepository.save(unit);
        Unit unit2 = new Unit();
        unit2.setId(2);
        unit2.setName("unit2");
        unit2.setOwner(employee);
        unitRepository.save(unit2);
        //Test
        employeeService.addToJoinedUnits(employee.getId(), unit);
        len = employee.getJoinedUnits().size();
        assertTrue(employeeService.addToJoinedUnits(employee.getId(),unit2));
        assertEquals(len + 1, employee.getJoinedUnits().size());

    }

    @Test
    void removeFromJoinedUnits() {
        //Dane
        Employee employee = simpleEventMocker.mockEmployee("test");
        int len;
        Unit unit = new Unit();
        unit.setId(1);
        unit.setName("unit1");
        unit.setOwner(employee);
        unitRepository.save(unit);
        Unit unit2 = new Unit();
        unit2.setId(2);
        unit2.setName("unit2");
        unit2.setOwner(employee);
        unitRepository.save(unit2);
        employeeService.addToJoinedUnits(employee.getId(), unit2);
        employeeService.addToJoinedUnits(employee.getId(), unit);
        //Test
        len = employee.getJoinedUnits().size();
        assertTrue(employeeService.removeFromJoinedUnits(employee.getId(),unit2));
        assertEquals(len -1 , employee.getJoinedUnits().size());
        assertFalse(employeeService.removeFromJoinedUnits(employee.getId(),unit2));
    }

    @Test
    void getJoinedUnits() {
        String ownerName = "owner";
        String unitName = "pros";
        String unitDesc = "unit for pros";
        String nextUnitName = "noobs";
        String nextUnitDesc = "unit for noobs";
        Employee owner = simpleEventMocker.mockEmployee(ownerName);
        Employee employee = simpleEventMocker.mockEmployee("employee");

        Unit unit = new Unit();
        unit.setName(unitName);
        unit.setDescription(unitDesc);
        unit.setOwner(owner);
        unitRepository.save(unit);

        Unit nextUnit = new Unit();
        nextUnit.setName(nextUnitName);
        nextUnit.setDescription(nextUnitDesc);
        nextUnit.setOwner(owner);
        unitRepository.save(nextUnit);

        assertEquals(HttpStatus.NOT_FOUND, employeeService.getListOfJoinedUnits(employee.getId()).getStatusCode());

        Set<Unit> joinedUnits = new HashSet<>();
        joinedUnits.add(unit);
        employee.setJoinedUnits(joinedUnits);

        assertEquals(HttpStatus.OK, employeeService.getListOfJoinedUnits(employee.getId()).getStatusCode());

        ResponseEntity<?> responseEntity = employeeService.getListOfJoinedUnits(employee.getId());
        Set<UnitToDisplayOnListDto> unitToDisplayOnList = (Set<UnitToDisplayOnListDto>) responseEntity.getBody();
        assertEquals(1,unitToDisplayOnList.size());
        assertEquals(unitName,unitToDisplayOnList.stream().findFirst().get().getName());
        assertNotEquals(nextUnit,unitToDisplayOnList.stream().findFirst().get().getName());
        assertEquals(HttpStatus.valueOf(404), employeeService.getSetOfOwnedEvents(4).getStatusCode());

        joinedUnits.add(nextUnit);
        employee.setJoinedUnits(joinedUnits);

        ResponseEntity<?> responseEntity2 = employeeService.getListOfJoinedUnits(employee.getId());
        Set<UnitToDisplayOnListDto> unitToDisplayOnList2 = (Set<UnitToDisplayOnListDto>) responseEntity2.getBody();
        assertEquals(2,unitToDisplayOnList2.size());
        assertTrue(((UnitToDisplayOnListDto)(((Set<?>) responseEntity2.getBody()).stream().findFirst().get())).getName().equals(unitName) ||
                ((UnitToDisplayOnListDto)(((Set<?>) responseEntity2.getBody()).stream().findFirst().get())).getName().equals(nextUnitName)
        );

    }
}