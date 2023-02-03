package pl.envelo.melo.domain.unit;

import jakarta.transaction.Transactional;
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

    public ResponseEntity<?> changeOwnership(int newEmployeeId, int currentTokentId, int unitId) {
        Optional<Unit> unit = unitRepository.findById(unitId);
        Optional<Employee> oldOwner = employeeRepository.findById(currentTokentId);
        Optional<Employee> newOwner = employeeRepository.findById(newEmployeeId);

        if (!unit.isPresent()) {
            return ResponseEntity.status(404).body("Unit does not exist");
        } else if (!oldOwner.isPresent()) {
            return ResponseEntity.status(404).body("Your id " + currentTokentId + " doesn't exist in data base");
        } else if(!newOwner.isPresent()){
            return ResponseEntity.status(404).body("Chosen employee with id " + newEmployeeId + " doesn't exist in data base");
        } else if (unit.get().getOwner().getId() != oldOwner.get().getId()){
                return ResponseEntity.status(400).body("You are not the organizer of the event you " +
                                                        "do not have the authority to make changes");
        } else {
            employeeService.removeFromOwnedUnits(currentTokentId, unit.get());
            unit.get().setOwner(newOwner.get());
            employeeService.addToJoinedUnits(newEmployeeId, unit.get());
            employeeService.addToOwnedUnits(newEmployeeId, unit.get());
            if (!unit.get().getMembers().contains(newOwner.get())) {
                unit.get().getMembers().add(newOwner.get());
            }
            return ResponseEntity.status(200).body("The owner of the unit with id "
                    + unitId + " has been correctly changed to "
                    + newOwner.get().getUser().getPerson().getFirstName() + " "
                    + newOwner.get().getUser().getPerson().getLastName());
        }
    }


    public ResponseEntity<?> addEmployee(int employeeId, int unitId) {
        if(employeeRepository.existsById(employeeId)){
            Optional<Unit> unit = unitRepository.findById(unitId);
            if(unit.isPresent()){
                if(employeeService.addToJoinedUnits(employeeId,unit.get())){
                    if(unit.get().getMembers()==null){
                        Set<Employee> members = new HashSet<>();
                        members.add(employeeRepository.findById(employeeId).get());
                        unit.get().setMembers(members);
                    }else {
                        unit.get().getMembers().add(employeeRepository.findById(employeeId).get());
                    }
                    unitRepository.save(unit.get());
                    return ResponseEntity.ok(true);
                }
                return ResponseEntity.status(400).body("Employee already in unit");
            }
            return ResponseEntity.status(404).body("Unit does not exist");
        }
        return ResponseEntity.status(404).body("Employee is not in database");
    }

    public ResponseEntity<?> quitUnit(Employee employee, int unitId) {
        return null;
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
        employeeRepository.save(employee);
        UnitToDisplayOnListDto unitReturn = unitMapper.convert(unitRepository.findById(unit.getId()).get());
        return ResponseEntity.ok(unitReturn);
    }

    public ResponseEntity<Unit> updateUnit(UnitToDisplayOnListDto unitToDisplayOnListDto) {
        return null;
    }
}
