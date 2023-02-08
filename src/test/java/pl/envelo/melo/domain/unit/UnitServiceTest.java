package pl.envelo.melo.domain.unit;

import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pl.envelo.melo.EventContextTest;
import pl.envelo.melo.authorization.employee.Employee;
import pl.envelo.melo.domain.unit.dto.UnitNewDto;
import pl.envelo.melo.domain.unit.dto.UnitToDisplayOnListDto;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UnitServiceTest extends EventContextTest{
    @Autowired
    UnitService unitService;
    @Autowired
    UnitRepository unitRepository;
    //@Test
    void getUnit() {
    }

    @Test
    void getUnits() {
        String ownerName = "owner";
        String unitName = "pros";
        String unitDesc = "unit for pros";
        String nextUnitName = "noobs";
        String nextUnitDesc = "unit for noobs";
        Employee owner = simpleEventMocker.mockEmployee(ownerName);
        Unit unit = new Unit();
        unit.setName(unitName);
        unit.setDescription(unitDesc);
        unit.setOwner(owner);
        unitRepository.save(unit);
        ResponseEntity<?> response = unitService.getUnits("");
        assertTrue(response.getBody() instanceof List<?>);
        assertEquals(1, ((List<?>) response.getBody()).size());
        Unit nextUnit = new Unit();
        nextUnit.setName(nextUnitName);
        nextUnit.setDescription(nextUnitDesc);
        nextUnit.setOwner(owner);
        unitRepository.save(nextUnit);
        response = unitService.getUnits("");
        assertTrue(response.getBody() instanceof List<?>);
        assertEquals(2, ((List<?>)response.getBody()).size());
        assertTrue(((List<?>) response.getBody()).stream().findFirst().isPresent());
        assertTrue(((UnitToDisplayOnListDto)(((List<?>) response.getBody()).stream().findFirst().get())).getName().equals(unitName) ||
                        ((UnitToDisplayOnListDto)(((List<?>) response.getBody()).stream().findFirst().get())).getName().equals(nextUnitName)
                );
    }

    //@Test
    void getUnitEmployees() {
    }

    @Test
    void changeOwnership() {
        String ownerName = "owner";
        String unitName = "pros";
        String unitDesc = "unit for pros";
        Employee owner = simpleEventMocker.mockEmployee(ownerName);
        Employee employee = simpleEventMocker.mockEmployee("Test");
        Unit unit = new Unit();
        unit.setName(unitName);
        unit.setDescription(unitDesc);
        unit.setOwner(owner);
        Set<Employee> members = new HashSet<>();
        members.add(owner);
        unit.setMembers(members);

        Set<Unit> joinedUnits = new HashSet<>();
        joinedUnits.add(unit);
        owner.setJoinedUnits(joinedUnits);

        unitRepository.save(unit);

        ResponseEntity<?> response = unitService.changeOwnership(employee.getId(),owner.getId(),unit.getId());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2,unit.getMembers().size());
        assertEquals(unit.getOwner().getId(),employee.getId());
        assertTrue(employee.getJoinedUnits().contains(unit));
        assertTrue(owner.getJoinedUnits().contains(unit));

        response = unitService.changeOwnership(owner.getId(),owner.getId(),unit.getId());
        assertEquals(HttpStatus.BAD_REQUEST,response.getStatusCode());


    }

    @Test
    void addEmployee() {
        //Dane
        String ownerName = "owner";
        String unitName = "pros";
        String unitDesc = "unit for pros";
        Employee owner = simpleEventMocker.mockEmployee(ownerName);
        Employee employee = simpleEventMocker.mockEmployee("Test");
        Unit unit = new Unit();
        unit.setName(unitName);
        unit.setDescription(unitDesc);
        unit.setOwner(owner);
        unitRepository.save(unit);
        //Test
        assertNull(unit.getMembers());
        ResponseEntity<?> response = unitService.addEmployee(owner.getId(), unit.getId());
        assertTrue(response.getBody() instanceof Boolean);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(unit.getMembers().contains(owner));
        assertEquals(1,unit.getMembers().size());
        ResponseEntity<?> response2 = unitService.addEmployee(employee.getId(), unit.getId());
        assertTrue(response2.getBody() instanceof Boolean);
        assertEquals(HttpStatus.OK, response2.getStatusCode());
        assertTrue(unit.getMembers().contains(employee));
        assertEquals(2,unit.getMembers().size());
        ResponseEntity<?> response3 = unitService.addEmployee(employee.getId(), unit.getId());
        assertEquals(HttpStatus.valueOf(400), response3.getStatusCode());

    }

    @Test
    void quitUnit() {
        String ownerName = "owner";
        String unitName = "pros";
        String unitDesc = "unit for pros";
        String nextUnitName = "noobs";
        String nextUnitDesc = "unit for noobs";
        Employee owner = simpleEventMocker.mockEmployee(ownerName);
        Employee test = simpleEventMocker.mockEmployee("test");
        Set<Employee> members = new HashSet<>();
        members.add(owner);
        members.add(test);

        Unit unit = new Unit();
        unit.setName(unitName);
        unit.setDescription(unitDesc);
        unit.setOwner(owner);
        unit.setMembers(members);
        unitRepository.save(unit);

        Set<Unit> joinedUnits = new HashSet<>();
        joinedUnits.add(unit);

        owner.setJoinedUnits(joinedUnits);
        test.setJoinedUnits(joinedUnits);

        assertEquals(unit.getMembers().size(),2);
        assertTrue(unit.getMembers().contains(owner));
        assertTrue(unit.getMembers().contains(test));
        assertFalse(owner.getJoinedUnits().isEmpty());
        assertFalse(test.getJoinedUnits().isEmpty());
        assertEquals(unit.getOwner(),owner);

        ResponseEntity<?> responseEntity = unitService.quitUnit(test.getId(),unit.getId());

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        assertEquals(unit.getMembers().size(),1);
        assertFalse(unit.getMembers().contains(test));
        assertTrue(unit.getMembers().contains(owner));
        assertTrue(test.getJoinedUnits().isEmpty());

        responseEntity = unitService.quitUnit(owner.getId(),unit.getId());

        assertEquals(HttpStatus.BAD_REQUEST,responseEntity.getStatusCode());

        assertEquals(unit.getMembers().size(),1);
        assertTrue(unit.getMembers().contains(owner));
        assertEquals(unit.getOwner(),owner);

    }

    @Test
    void insertNewUnit() {
        //Dane
        String ownerName = "owner";
        String unitName = "pros";
        String unitDesc = "unit for pros";
        Employee owner = simpleEventMocker.mockEmployee(ownerName);
        UnitNewDto unit = new UnitNewDto();
        unit.setName(unitName);
        unit.setDescription(unitDesc);
        ResponseEntity<?> unit1 = unitService.insertNewUnit(unit);
        String unitName2 = "Prosto     ";
        String unitDesc2 = "unit      for pros";
        UnitNewDto unit2 = new UnitNewDto();
        unit2.setName(unitName2);
        unit2.setDescription(unitDesc2);
        //Test
        assertTrue(unit1.getBody() instanceof UnitToDisplayOnListDto);
        assertEquals(HttpStatus.OK, unit1.getStatusCode());
        assertEquals(unitName, ((UnitToDisplayOnListDto) unit1.getBody()).getName());
        assertEquals(HttpStatus.valueOf(400), unitService.insertNewUnit(unit).getStatusCode());
        ResponseEntity<?> unit3 = unitService.insertNewUnit(unit2);
        assertTrue(unit3.getBody() instanceof UnitToDisplayOnListDto);
        assertEquals("prosto", ((UnitToDisplayOnListDto) unit3.getBody()).getName());
        assertEquals("unit for pros", ((UnitToDisplayOnListDto) unit3.getBody()).getDescription());
    }

    //@Test
    void updateUnit() {
    }

    @Test
    void changeOwnershipByAdmin() {
        Employee firstOwner = simpleEventMocker.mockEmployee("test");
        Employee nextOwner = simpleEventMocker.mockEmployee("test");
        Unit unit = new Unit();
        unit.setName("TEST");
        unit.setDescription("TEST");
        unit.setOwner(firstOwner);
        firstOwner.setJoinedUnits(new HashSet<>(Set.of(unit)));
        firstOwner.setOwnedUnits(new HashSet<>(Set.of(unit)));
        unitRepository.save(unit);
        assertEquals(firstOwner, unit.getOwner());
        unitService.changeOwnershipByAdmin(unit.getId(), nextOwner.getId());
        assertEquals(nextOwner, unit.getOwner());
        assertEquals(0, firstOwner.getOwnedUnits().size());
        assertEquals(1, firstOwner.getJoinedUnits().size());
        assertEquals(1, nextOwner.getJoinedUnits().size());
        assertEquals(1, nextOwner.getOwnedUnits().size());
        assertTrue(unit.getMembers().contains(nextOwner));
        assertTrue(unit.getMembers().contains(firstOwner));
    }
}