package pl.envelo.melo.authorization.employee;


import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.envelo.melo.authorization.employee.dto.EmployeeDto;
import pl.envelo.melo.authorization.person.Person;
import pl.envelo.melo.domain.unit.Unit;
import pl.envelo.melo.mappers.EmployeeMapper;
import pl.envelo.melo.authorization.person.PersonRepository;
import pl.envelo.melo.domain.event.Event;
import pl.envelo.melo.mappers.EventMapper;
import pl.envelo.melo.mappers.UnitMapper;

import java.util.*;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class EmployeeService {


    private EmployeeRepository employeeRepository;
    private final EventMapper eventMapper;
    private final UnitMapper unitMapper;
    private PersonRepository personRepository;
    private final EmployeeMapper employeeMapper;


    public ResponseEntity<EmployeeDto> getEmployee(int id) {

        Optional<Employee> employeeOptional = employeeRepository.findById(id);
        if (!employeeOptional.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        EmployeeDto employeeDto = employeeMapper.toDto(employeeOptional.get());
        return new ResponseEntity<>(employeeDto, HttpStatus.OK);

    }


    public ResponseEntity<Person> getPerson(int employeeId) {
//        return personRepository.findById(employeeId);
        return null;
    }

    public ResponseEntity<List<EmployeeDto>> getEmployees() {
//        return employeeRepository.findAll();
        return null;
    }

    public boolean addToOwnedEvents(int employeeId, Event event) {
        if (employeeRepository.existsById(employeeId)) {
            Set<Event> ownedEvent = employeeRepository.findById(employeeId).get().getOwnedEvents();
            if (ownedEvent == null) {
                ownedEvent = new HashSet<>();
                ownedEvent.add(event);
                employeeRepository.findById(employeeId).get().setOwnedEvents(ownedEvent);
            } else {
                employeeRepository.findById(employeeId).get().getOwnedEvents().add(event);
            }
            return true;
        }
        return false;

    }

    public boolean removeFromOwnedEvents(int employeeId, Event event) {
        if (employeeRepository.existsById(employeeId)) {
            Set<Event> ownedEvent = employeeRepository.findById(employeeId).get().getOwnedEvents();
            if (ownedEvent != null && ownedEvent.contains(event)) {
                employeeRepository.findById(employeeId).get().getOwnedEvents().remove(event);
                return true;
            }
        }
        return false;
    }

    public boolean addToJoinedEvents(int employeeId, Event event) {
        if (employeeRepository.existsById(employeeId)) {
            Set<Event> joinedEvent = employeeRepository.findById(employeeId).get().getJoinedEvents();
            if (joinedEvent == null) {
                joinedEvent = new HashSet<>();
                joinedEvent.add(event);
                employeeRepository.findById(employeeId).get().setJoinedEvents(joinedEvent);
            } else {
                employeeRepository.findById(employeeId).get().getJoinedEvents().add(event);
            }
            return true;
        }
        return false;
    }

    public boolean removeFromJoinedEvents(int employeeId, Event event) {
        if (employeeRepository.existsById(employeeId) && event.getOrganizer().getId() != employeeId) {
            Set<Event> joinedEvent = employeeRepository.findById(employeeId).get().getJoinedEvents();
            if (joinedEvent != null && joinedEvent.contains(event)) {
                employeeRepository.findById(employeeId).get().getJoinedEvents().remove(event);
                return true;
            }
        }
        return false;
    }

    public boolean addToOwnedUnits(int employeeId, Unit unit) {
        if (employeeRepository.existsById(employeeId)) {
            Set<Unit> ownedUnit = employeeRepository.findById(employeeId).get().getOwnedUnits();
            if (ownedUnit == null) {
                ownedUnit = new HashSet<>();
                ownedUnit.add(unit);
                employeeRepository.findById(employeeId).get().setOwnedUnits(ownedUnit);
            } else {
                employeeRepository.findById(employeeId).get().getOwnedUnits().add(unit);
            }
            return true;
        }
        return false;
    }

    public boolean removeFromOwnedUnits(int employeeId, Unit unit) {
        if (employeeRepository.existsById(employeeId)) {
            Set<Unit> ownedUnits = employeeRepository.findById(employeeId).get().getOwnedUnits();
            if (ownedUnits != null && ownedUnits.contains(unit)) {
                employeeRepository.findById(employeeId).get().getOwnedUnits().remove(unit);
                return true;
            }
        }
        return false;
    }

    public boolean addToJoinedUnits(int employeeId, Unit unit) {
        if (employeeRepository.existsById(employeeId)) {
            Set<Unit> joinedUnits = employeeRepository.findById(employeeId).get().getJoinedUnits();
            if (joinedUnits == null) {
                joinedUnits = new HashSet<>();
                joinedUnits.add(unit);
                employeeRepository.findById(employeeId).get().setJoinedUnits(joinedUnits);
            }
            employeeRepository.findById(employeeId).get().getJoinedUnits().add(unit);
            return true;
        }
        return false;
    }

    public boolean removeFromJoinedUnits(int employeeId, Unit unit) {
        if (employeeRepository.existsById(employeeId)) {
            Set<Unit> joinedUnit = employeeRepository.findById(employeeId).get().getJoinedUnits();
            if (joinedUnit != null && joinedUnit.contains(unit)) {
                employeeRepository.findById(employeeId).get().getJoinedUnits().remove(unit);
                return true;
            }
        }
        return false;
    }


    public ResponseEntity<?> getSetOfOwnedEvents(int id) {
        if (employeeRepository.existsById(id)) {
            Set<Event> events = employeeRepository.findById(id).get().getOwnedEvents();
            return ResponseEntity.ok(events.stream().map(eventMapper::convert).collect(Collectors.toSet()));
        } else {
            return ResponseEntity.status(404).body("Employee with this ID do not exist");
        }

    }

    public ResponseEntity<?> getListOfCreatedUnits(int employeeId){
        if(employeeRepository.existsById(employeeId)){
            return ResponseEntity.ok(employeeRepository.findById(employeeId).get().getOwnedUnits().stream().map(unitMapper::convert).collect(Collectors.toSet()));
        }
        else{
            return ResponseEntity.status(404).body("Employee with this ID do not exist");
        }
    }
}
