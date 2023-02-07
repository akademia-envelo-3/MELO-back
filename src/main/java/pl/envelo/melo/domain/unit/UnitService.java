package pl.envelo.melo.domain.unit;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.envelo.melo.authorization.employee.Employee;
import pl.envelo.melo.authorization.employee.EmployeeRepository;
import pl.envelo.melo.authorization.employee.EmployeeService;
import pl.envelo.melo.domain.unit.dto.UnitToDisplayOnListDto;
import pl.envelo.melo.domain.unit.dto.UnitNewDto;
import pl.envelo.melo.mappers.UnitDetailsMapper;
import pl.envelo.melo.mappers.UnitMapper;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UnitService {

    private final UnitRepository unitRepository;
    private final EmployeeRepository employeeRepository;
    private final EmployeeService employeeService;
    private final UnitMapper unitMapper;
    private final UnitDetailsMapper unitDetailsMapper;

    public ResponseEntity<?> getUnit(int id) {
        Optional<Unit> unit = unitRepository.findById(id);
        if(unit.isPresent()){
            return ResponseEntity.ok(unitDetailsMapper.convert(unit.get()));
        }
        return ResponseEntity.notFound().build();
    }

    public ResponseEntity<?> getUnits() {
        return ResponseEntity.ok(unitRepository.findAll().stream().map(e->{
            UnitToDisplayOnListDto dto = unitMapper.convert(e);
            return dto;
        }).collect(Collectors.toList()));
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

    public ResponseEntity<?> quitUnit(int employeeIdToken, int unitId) {
        Optional<Unit> unit = unitRepository.findById(unitId);
        Optional<Employee> employee = employeeRepository.findById(employeeIdToken);
        if (unit.isPresent()){
            if (unit.get().getOwner().getId() == employeeIdToken){
                return ResponseEntity.status(400).body("Unit organizer cant be remove from his unit");
            }
            if (employee.isPresent() && unit.get().getMembers().contains(employee.get())){
                unit.get().getMembers().remove(employee.get());
                employeeService.removeFromJoinedUnits(employeeIdToken, unit.get());
                return ResponseEntity.ok("Employee whit Id "+ employeeIdToken +
                        " was correctly removed from the members of the unit");
            }
            else if (employee.isEmpty()){
                return ResponseEntity.status(404).body("Employee whit Id "+ employeeIdToken + " does not exist");
            }
            else
                return ResponseEntity.status(404).body("Employee is not a member of the unit");
        }
        else
            return ResponseEntity.status(404).body("Unit whit Id "+ unitId + " does not exist");
    }

    public ResponseEntity<?> insertNewUnit(UnitNewDto unitNewDto) {
        int employeeId =1;//TODO Wyciągnąc z tokena
        Unit unit = unitMapper.toEntity(unitNewDto);
        unit.setName(unit.getName().replaceAll("( +)", " ").trim().toLowerCase());
        if(employeeRepository.findById(employeeId).isEmpty()){
            return ResponseEntity.status(404).body("Employee is not in Database");
        }
        if(unitRepository.findByName(unit.getName().toLowerCase()).isPresent()){
            return ResponseEntity.status(400).body("Unit with this name already exist");
        }
        unit.setDescription(unit.getDescription().replaceAll("( +)", " ").trim());
        Employee employee = employeeRepository.findById(employeeId).get();
        unit.setOwner(employee);
        Set<Employee> members = new HashSet<>();
        members.add(employee);
        unit.setMembers(members);
        unitRepository.save(unit);
        employeeService.addToOwnedUnits(employee.getId(),unit);
        employeeService.addToJoinedUnits(employee.getId(),unit);
        employeeRepository.save(employee);
        UnitToDisplayOnListDto unitReturn = unitMapper.convert(unitRepository.findById(unit.getId()).get());
        return ResponseEntity.ok(unitReturn);
    }

    public ResponseEntity<Unit> updateUnit(UnitToDisplayOnListDto unitToDisplayOnListDto) {
        return null;
    }
}
