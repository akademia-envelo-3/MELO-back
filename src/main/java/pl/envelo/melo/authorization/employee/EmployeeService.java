package pl.envelo.melo.authorization.employee;


import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.envelo.melo.authorization.AuthorizationService;
import pl.envelo.melo.authorization.employee.dto.EmployeeDto;
import pl.envelo.melo.authorization.employee.dto.EmployeeListDto;
import pl.envelo.melo.authorization.person.Person;
import pl.envelo.melo.domain.event.Event;
import pl.envelo.melo.domain.unit.Unit;
import pl.envelo.melo.domain.unit.dto.UnitToDisplayOnListDto;
import pl.envelo.melo.exceptions.EmployeeNotFoundException;
import pl.envelo.melo.mappers.EmployeeListMapper;
import pl.envelo.melo.mappers.EmployeeMapper;
import pl.envelo.melo.mappers.EventMapper;
import pl.envelo.melo.mappers.UnitMapper;

import java.security.Principal;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class EmployeeService {
    private EmployeeRepository employeeRepository;
    private final EventMapper eventMapper;
    private final UnitMapper unitMapper;
    private final EmployeeMapper employeeMapper;
    private final EmployeeListMapper employeeListMapper;
    private final AuthorizationService authorizationService;

    public ResponseEntity<EmployeeDto> getEmployee(int id, Principal principal) {
        Employee employee = employeeRepository.findByUserId(authorizationService.getUUID(principal)).orElseThrow(EmployeeNotFoundException::new);
        if (id != employee.getId())
            return ResponseEntity.status(403).build();
        EmployeeDto employeeDto = employeeMapper.toDto(employee);
        return new ResponseEntity<>(employeeDto, HttpStatus.OK);

    }


    public ResponseEntity<Person> getPerson(int employeeId) {
//        return personRepository.findById(employeeId);
        return null;
    }

    public ResponseEntity<Set<EmployeeListDto>> getEmployees(String q) {
        if (q == null) {
            return ResponseEntity.ok(employeeRepository.findAll().stream().map(employeeListMapper::toDto).collect(Collectors.toSet()));
        } else {
            String[] listQ = q.split(" ");
            Set<Employee> employeeSet = new HashSet<>();
            if (listQ.length == 1) {
                employeeSet.addAll(employeeRepository.findByUserPersonFirstNameContainingIgnoreCaseOrUserPersonLastNameContainingIgnoreCase(listQ[0], listQ[0]).get());
            } else if (listQ.length == 2) {
                employeeSet.addAll(employeeRepository.findByUserPersonFirstNameContainingIgnoreCaseAndUserPersonLastNameContainingIgnoreCase(listQ[0], listQ[1]).get());
            }
            return ResponseEntity.ok(employeeSet.stream().map(employeeListMapper::toDto).collect(Collectors.toSet()));
        }
    }

    public boolean addToOwnedEvents(int employeeId, Event event) {
        if (employeeRepository.existsById(employeeId)) {
            Set<Event> ownedEvent = employeeRepository.findById(employeeId).get().getOwnedEvents();
            if (ownedEvent == null) {
                ownedEvent = new HashSet<>();
                ownedEvent.add(event);
                employeeRepository.findById(employeeId).get().setOwnedEvents(ownedEvent);
            } else {
                if (ownedEvent.contains(event)) {
                    return false;
                }
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
                if (joinedEvent.contains(event)) {
                    return false;
                }
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
                if (ownedUnit.contains(unit)) {
                    return false;
                }
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
            } else {
                if (joinedUnits.contains(unit)) {
                    return false;
                }
                employeeRepository.findById(employeeId).get().getJoinedUnits().add(unit);
            }
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


    public ResponseEntity<?> getSetOfOwnedEvents(int id, Principal principal) {
        Employee employee = employeeRepository.findByUserId(authorizationService.getUUID(principal)).orElseThrow(EmployeeNotFoundException::new);
        if (id != employee.getId())
            return ResponseEntity.status(403).build();
        Set<Event> events = employee.getOwnedEvents();
        return ResponseEntity.ok(events.stream().map(eventMapper::convert).collect(Collectors.toSet()));
    }

    public ResponseEntity<?> getListOfJoinedUnits(int id, Principal principal) {
        Employee employee = employeeRepository.findByUserId(authorizationService.getUUID(principal)).orElseThrow(EmployeeNotFoundException::new);
        if (id != employee.getId())
            return ResponseEntity.status(403).build();
        if (employee.getJoinedUnits() == null) {
            return ResponseEntity.status(404).body("No units to display");
        } else {
            return ResponseEntity.ok(employeeRepository.findById(id).get()
                    .getJoinedUnits()
                    .stream()
                    .map(unitMapper::convert)
                    .collect(Collectors.toSet()));
        }
    }

    public ResponseEntity<?> getListOfCreatedUnits(int employeeId, Principal principal) {
        Employee employee = employeeRepository.findByUserId(authorizationService.getUUID(principal)).orElseThrow(EmployeeNotFoundException::new);
        if (employeeId != employee.getId())
            return ResponseEntity.status(403).build();
        return ResponseEntity.ok(employee.getOwnedUnits().stream().map(e -> {
            UnitToDisplayOnListDto dto = unitMapper.convert(e);
            return dto;
        }).collect(Collectors.toSet()));

    }
}
