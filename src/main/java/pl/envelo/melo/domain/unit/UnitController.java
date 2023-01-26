package pl.envelo.melo.domain.unit;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.envelo.melo.authorization.employee.Employee;
import pl.envelo.melo.domain.unit.dto.UnitDto;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/v1/units")
public class UnitController {

    private final UnitService unitService;


    public ResponseEntity<UnitDto> getUnit(int id) {
        return unitService.getUnit(id);
    }


    public ResponseEntity<List<UnitDto>> getUnits() {
        return unitService.getUnits();
    }


    public ResponseEntity<List<Employee>> getUnitEmployees() {
        return unitService.getUnitEmployees();
    }


    public ResponseEntity<Unit> changeOwnership(int newEmployeeId) {
        return unitService.changeOwnership(newEmployeeId);
    }


    public ResponseEntity<?> addEmployee(Employee employee, int unitId) {
        return unitService.addEmployee(employee, unitId);
    }


    public ResponseEntity<?> quitUnit(Employee employee, int unitId) {
        return unitService.quitUnit(employee, unitId);
    }


    @PostMapping("")
    public ResponseEntity<?> addNewUnit(@RequestBody @Valid UnitDto unitDto) {
        return unitService.insertNewUnit(unitDto);
    }

    public ResponseEntity<Unit> updateUnit(UnitDto unitDto) {
        return unitService.updateUnit(unitDto);
    }


}
