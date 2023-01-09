package pl.envelo.melo.domain.unit;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.envelo.melo.authorization.employee.Employee;
import pl.envelo.melo.domain.unit.dto.UnitDto;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UnitService {

    private final UnitRepository unitRepository;
    private final EmployeeRepository employeeRepository;


    public Optional<UnitDto> getUnit(int id) {
        return null;
    }

    public List<UnitDto> getUnits() {
        return null;
    }

    public List<Employee> getUnitEmployees() {
        return null;
    }

    public Optional<Unit> changeOwnership(int newEmployeeId) {
        return null;
    }

    public boolean addEmployee(Employee employee, int unitId) {
        return false;
    }

    public boolean quitUnit(Employee employee, int unitId) {
        return false;
    }

    public Optional<Unit> insertNewUnit(UnitDto unitDto) {
        return null;
    }

    public Optional<Unit> updateUnit(UnitDto unitDto) {
        return null;
    }
}
