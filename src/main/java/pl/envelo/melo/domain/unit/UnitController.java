package pl.envelo.melo.domain.unit;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.envelo.melo.authorization.employee.Employee;
import pl.envelo.melo.domain.unit.dto.UnitDto;

import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
public class UnitController {

    private final UnitService unitService;

    @GetMapping
    public Optional<UnitDto> getUnit(int id) {
        return unitService.getUnit(id);
    }

    @GetMapping
    public List<UnitDto> getUnits() {
        return unitService.getUnits();
    }

    @GetMapping
    public List<Employee> getUnitEmployees() {
        return unitService.getUnitEmployees();
    }

    @PatchMapping
    public Optional<Unit> changeOwnership(int newEmployeeId) {
        return unitService.changeOwnership(newEmployeeId);
    }

    @PostMapping
    public boolean addEmployee(Employee employee, int unitId) {
        return unitService.addEmployee(employee, unitId);
    }

//    @PatchMapping
    public boolean quitUnit(Employee employee, int unitId) {
        return unitService.quitUnit(employee, unitId);
    }

    @PostMapping
    public Optional<Unit> addNewUnit(UnitDto unitDto) {
        return unitService.insertNewUnit(unitDto);
    }

    @PatchMapping
    public Optional<Unit> updateUnit(UnitDto unitDto) {
        return unitService.updateUnit(unitDto);
    }


}
