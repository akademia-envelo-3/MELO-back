package pl.envelo.melo.domain.unit;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.envelo.melo.authorization.employee.Employee;
import pl.envelo.melo.authorization.employee.EmployeeRepository;
import pl.envelo.melo.authorization.employee.EmployeeService;
import pl.envelo.melo.domain.unit.dto.UnitDto;
import pl.envelo.melo.mappers.UnitMapper;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@AllArgsConstructor
public class UnitService {

    private final UnitRepository unitRepository;
    private final EmployeeRepository employeeRepository;
    private final EmployeeService employeeService;
    private UnitMapper unitMapper;


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

    public ResponseEntity<?> insertNewUnit(UnitDto unitDto) {
        int employeeId =1;//TODO Wyciągnąc z tokena
        Unit unit = unitMapper.toEntity(unitDto);
        if(employeeRepository.findById(employeeId).isEmpty()){
            return ResponseEntity.status(404).body("Employee is not in Database");
        }
        if(unitRepository.findByName(unit.getName().toLowerCase()).isPresent()){
            return ResponseEntity.status(400).body("Unit with this name already exist");
        }
        unit.setName(unit.getName().replaceAll("( +)", " ").trim().toLowerCase());
        unit.setDescription(unit.getDescription().replaceAll("( +)", " ").trim().toLowerCase());
        Employee employee = employeeRepository.findById(employeeId).get();
        unit.setOwner(employee);
        Set<Employee> members = new HashSet<>();
        members.add(employee);
        unit.setMembers(members);
        unitRepository.save(unit);
        employeeService.addToOwnedUnits(employee.getId(),unit);
        employeeRepository.save(employee);
        return ResponseEntity.ok(unitMapper.toDto(unitRepository.findById(unit.getId()).get()));
    }

    public ResponseEntity<Unit> updateUnit(UnitDto unitDto) {
        return null;
    }
}
