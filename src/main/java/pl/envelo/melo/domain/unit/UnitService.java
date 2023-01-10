package pl.envelo.melo.domain.unit;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.envelo.melo.authorization.employee.Employee;
import pl.envelo.melo.authorization.employee.EmployeeRepository;
import pl.envelo.melo.domain.unit.dto.UnitDto;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UnitService {

    private final UnitRepository unitRepository;
    private final EmployeeRepository employeeRepository;


    public ResponseEntity<UnitDto> getUnit(int id) {
        return null;
    }

    public ResponseEntity<List<UnitDto>> getUnits() {
        return null;
    }

    public ResponseEntity<List<Employee>> getUnitEmployees() {
        return null;
    }

    public ResponseEntity<Unit> changeOwnership(int newEmployeeId) {
        return null;
    }

    public ResponseEntity<?> addEmployee(Employee employee, int unitId) {
        return null;
    }

    public ResponseEntity<?> quitUnit(Employee employee, int unitId) {
        return null;
    }

    public ResponseEntity<Unit> insertNewUnit(UnitDto unitDto) {
        return null;
    }

    public ResponseEntity<Unit> updateUnit(UnitDto unitDto) {
        return null;
    }
}
