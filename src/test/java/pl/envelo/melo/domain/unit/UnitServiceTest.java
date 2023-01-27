package pl.envelo.melo.domain.unit;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import pl.envelo.melo.EventContextTest;
import pl.envelo.melo.authorization.employee.Employee;
import pl.envelo.melo.domain.unit.dto.UnitToDisplayOnListDto;

import java.util.List;

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
        ResponseEntity<?> response = unitService.getUnits();
        assertTrue(response.getBody() instanceof List<?>);
        assertEquals(1, ((List<?>) response.getBody()).size());
        Unit nextUnit = new Unit();
        nextUnit.setName(nextUnitName);
        nextUnit.setDescription(nextUnitDesc);
        nextUnit.setOwner(owner);
        unitRepository.save(nextUnit);
        response = unitService.getUnits();
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

    //@Test
    void changeOwnership() {
    }

    //@Test
    void addEmployee() {
    }

    //@Test
    void quitUnit() {
    }

    //@Test
    void insertNewUnit() {
    }

    //@Test
    void updateUnit() {
    }
}