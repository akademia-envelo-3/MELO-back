package pl.envelo.melo.domain.unit;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.envelo.melo.authorization.AuthorizationService;
import pl.envelo.melo.authorization.admin.Admin;
import pl.envelo.melo.authorization.admin.AdminRepository;
import pl.envelo.melo.authorization.employee.Employee;
import pl.envelo.melo.authorization.employee.EmployeeRepository;
import pl.envelo.melo.authorization.employee.EmployeeService;
import pl.envelo.melo.domain.event.EventConst;
import pl.envelo.melo.domain.notification.NotificationService;
import pl.envelo.melo.domain.notification.NotificationType;
import pl.envelo.melo.domain.notification.dto.UnitNotificationDto;
import pl.envelo.melo.domain.unit.dto.UnitNewDto;
import pl.envelo.melo.domain.unit.dto.UnitToDisplayOnListDto;
import pl.envelo.melo.exceptions.EmployeeNotFoundException;
import pl.envelo.melo.mappers.UnitDetailsMapper;
import pl.envelo.melo.mappers.UnitMapper;

import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

import static pl.envelo.melo.domain.unit.UnitConst.*;

@Service
@AllArgsConstructor
public class UnitService {

    private final UnitRepository unitRepository;
    private final EmployeeRepository employeeRepository;
    private final AdminRepository adminRepository;
    private final EmployeeService employeeService;
    private final UnitMapper unitMapper;
    private final UnitDetailsMapper unitDetailsMapper;
    private final NotificationService notificationService;
    private final AuthorizationService authorizationService;

    public ResponseEntity<?> getUnit(int id) {
        Optional<Unit> unit = unitRepository.findById(id);
        if (unit.isPresent()) {
            return ResponseEntity.ok(unitDetailsMapper.convert(unit.get()));
        }
        return ResponseEntity.notFound().build();
    }


    public ResponseEntity<?> getUnits(String text) {
        List<Unit> units;
        if (text == null || text.isEmpty()) {
            units = unitRepository.findAll();
        } else {
            units = unitRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(text, text);
        }

        return ResponseEntity.ok(units.stream()
                .sorted((u1, u2) -> Integer.compare(u2.getMembers().size(), u1.getMembers().size()))
                .map(unitMapper::convert).collect(Collectors.toList()));
    }

    public ResponseEntity<List<Employee>> getUnitEmployees() {
        return null;
    }

    public ResponseEntity<?> changeOwnership(int newEmployeeId, int unitId, Principal principal) {
        Employee oldOwner = employeeRepository.findByUserId(authorizationService.getUUID(principal)).orElse(null);
        Admin admin = adminRepository.findByUserId(authorizationService.getUUID(principal)).orElse(null);
        if (Objects.nonNull(admin))
            return changeOwnershipByAdmin(unitId, newEmployeeId);
        if (Objects.nonNull(oldOwner))
            return changeOwnershipByEmployee(newEmployeeId, unitId, oldOwner);
        return ResponseEntity.status(403).build();
    }

    @Transactional
    public ResponseEntity<?> changeOwnershipByEmployee(int newEmployeeId, int unitId, Employee oldOwner) {

        Optional<Unit> unit = unitRepository.findById(unitId);
        Optional<Employee> newOwner = employeeRepository.findById(newEmployeeId);

        if (!unit.isPresent()) {
            return ResponseEntity.status(404).body(UNIT_DOES_NOT_EXIST);
        } else if (!newOwner.isPresent()) {
            return employeeNotFound(newEmployeeId);
        } else if (unit.get().getOwner().getId() != oldOwner.getId()) {
            return ResponseEntity.status(400).body(EventConst.UNAUTHORIZED_EMPLOYEE);
        } else if (newEmployeeId == oldOwner.getId()) {
            return ResponseEntity.status(400).body("You can't assign ownership to yourself.");
        } else {
            employeeService.removeFromOwnedUnits(oldOwner.getId(), unit.get());
            unit.get().setOwner(newOwner.get());
            employeeService.addToJoinedUnits(newEmployeeId, unit.get());
            employeeService.addToOwnedUnits(newEmployeeId, unit.get());
            unit.get().getMembers().add(newOwner.get());
            sendOwnershipNotification(oldOwner.getId(), unit.get().getId(), true);
            sendOwnershipNotification(newEmployeeId, unit.get().getId(), false);
            unitRepository.save(unit.get());
            return unitOwnershipChanged(unitId, newOwner.get());
        }
    }

    private ResponseEntity<?> employeeNotFound(int employeeId) {
        return ResponseEntity.status(404).body("Chosen employee with id " + employeeId + " doesn't exist in data base");

    }

    private ResponseEntity<?> unitOwnershipChanged(int unitId, Employee newOwner) {
        return ResponseEntity.status(200).body("The owner of the unit with id "
                + unitId + " has been correctly changed to "
                + newOwner.getUser().getPerson().getFirstName() + " "
                + newOwner.getUser().getPerson().getLastName());
    }

    public ResponseEntity<?> changeOwnershipByAdmin(int unitId, int nextOwnerId) {
        Optional<Unit> unit = unitRepository.findById(unitId);
        Optional<Employee> nextOwner = employeeRepository.findById(nextOwnerId);
        if (unit.isEmpty() || nextOwner.isEmpty())
            return ResponseEntity.status(404).body(ONE_OF_THE_ENTITIES_NOT_FOUND);
        if (unit.get().getOwner().getId() == nextOwnerId)
            return ResponseEntity.status(400).body("This employee is already an owner.");
        Employee currentOwner = unit.get().getOwner();
        unit.get().setOwner(nextOwner.get());
        employeeService.removeFromOwnedUnits(currentOwner.getId(), unit.get());
        employeeService.addToOwnedUnits(nextOwnerId, unit.get());
        employeeService.addToJoinedUnits(nextOwnerId, unit.get());
        addMemberToUnit(nextOwner.get(), unit.get());
        unitRepository.save(unit.get());
        sendOwnershipNotification(currentOwner.getId(), unit.get().getId(), true);
        sendOwnershipNotification(nextOwnerId, unit.get().getId(), false);
        return ResponseEntity.ok().build();
    }

    private boolean addMemberToUnit(Employee employee, Unit unit) {
        if (Objects.isNull(employee) || Objects.isNull(unit))
            return false;
        if (Objects.isNull(unit.getMembers()))
            unit.setMembers(new HashSet<>());
        return unit.getMembers().add(employee);
    }

    private void sendOwnershipNotification(int employeeId, int unitId, boolean revoke) {
        UnitNotificationDto unitNotificationDto = new UnitNotificationDto();
        unitNotificationDto.setUnitId(unitId);
        unitNotificationDto.setEmployeeId(employeeId);
        if (revoke)
            unitNotificationDto.setNotificationType(NotificationType.UNIT_OWNERSHIP_REVOKED);
        else
            unitNotificationDto.setNotificationType(NotificationType.UNIT_OWNERSHIP_GRANTED);
        notificationService.insertUnitOwnerChangeNotification(unitNotificationDto);
    }


    public ResponseEntity<?> addEmployee(int unitId, Principal principal) {
        Employee employee = employeeRepository.findByUserId(authorizationService.getUUID(principal)).orElseThrow(EmployeeNotFoundException::new);
        Optional<Unit> unit = unitRepository.findById(unitId);
        if (unit.isPresent()) {
            if (employeeService.addToJoinedUnits(employee.getId(), unit.get())) {
                if (unit.get().getMembers() == null) {
                    Set<Employee> members = new HashSet<>();
                    members.add(employee);
                    unit.get().setMembers(members);
                } else {
                    unit.get().getMembers().add(employee);
                }
                unitRepository.save(unit.get());
                return ResponseEntity.ok(true);
            }
            return ResponseEntity.status(400).body(EMPLOYEE_ALREADY_IN_UNIT);
        }
        return ResponseEntity.status(404).body(UNIT_DOES_NOT_EXIST);

    }

    public ResponseEntity<?> quitUnit(int unitId, Principal principal) {
        Optional<Unit> unit = unitRepository.findById(unitId);
        Employee employee = employeeRepository.findByUserId(authorizationService.getUUID(principal)).orElseThrow(EmployeeNotFoundException::new);

        if (unit.isPresent()) {
            if (unit.get().getOwner().getId() == employee.getId()) {
                return ResponseEntity.status(400).body(UNIT_ORGANIZER_LEAVE_ATTEMPT);
            }
            if (unit.get().getMembers().contains(employee)) {
                unit.get().getMembers().remove(employee);
                employeeService.removeFromJoinedUnits(employee.getId(), unit.get());
                return employeeSuccessfulLeave(employee.getId());
            } else
                return ResponseEntity.status(404).body(EMPLOYEE_NOT_IN_MEMBER_LIST);
        } else
            return unitDoesNotExist(unitId);
    }

    private ResponseEntity<?> unitDoesNotExist(int unitId) {
        return ResponseEntity.status(404).body("Unit whit Id " + unitId + " does not exist");
    }

    private ResponseEntity<?> employeeSuccessfulLeave(int employeeId) {
        return ResponseEntity.ok("Employee whit Id " + employeeId +
                " was correctly removed from the members of the unit");
    }

    public ResponseEntity<?> insertNewUnit(UnitNewDto unitNewDto, Principal principal) {
        Employee employee = employeeRepository.findByUserId(authorizationService.getUUID(principal)).orElseThrow(EmployeeNotFoundException::new);
        Unit unit = unitMapper.toEntity(unitNewDto);
        unit.setName(unit.getName().replaceAll("( +)", " ").trim().toLowerCase());
        if (unitRepository.findByName(unit.getName().toLowerCase()).isPresent()) {
            return ResponseEntity.status(400).body(AMBIGUOUS_UNIT_NAME);
        }
        unit.setDescription(unit.getDescription().replaceAll("( +)", " ").trim());
        unit.setOwner(employee);
        Set<Employee> members = new HashSet<>();
        members.add(employee);
        unit.setMembers(members);
        unitRepository.save(unit);
        employeeService.addToOwnedUnits(employee.getId(), unit);
        employeeService.addToJoinedUnits(employee.getId(), unit);
        employeeRepository.save(employee);
        UnitToDisplayOnListDto unitReturn = unitMapper.convert(unitRepository.findById(unit.getId()).get());
        return ResponseEntity.ok(unitReturn);
    }

    public ResponseEntity<?> updateUnit(int unitId, UnitNewDto unitNewDto, Principal principal) {
        Employee employee = employeeRepository.findByUserId(authorizationService.getUUID(principal)).orElseThrow(EmployeeNotFoundException::new);
        Unit unit;
        if (unitRepository.findById(unitId).isPresent()) {
            unit = unitRepository.findById(unitId).get();
        } else {
            return ResponseEntity.status(404).body(UNIT_DOES_NOT_EXIST);
        }
        if (unit.getOwner() != employee) {
            return ResponseEntity.status(403).build();
        }
        NotificationType notification = null;
        if (!Objects.isNull(unitNewDto.getName())) {
            unitNewDto.setName(unitNewDto.getName().replaceAll("( +)", " ").trim().toLowerCase());
            if (!unitNewDto.getName().equals(unit.getName())) {
                unit.setName(unitNewDto.getName());
                notification = NotificationType.UNIT_NAME_UPDATED;
            } else {
                return ResponseEntity.status(400).body(UNIT_NAME_NOT_CHANGED);
            }
        }
        if (!Objects.isNull(unitNewDto.getDescription())) {
            unitNewDto.setDescription(unitNewDto.getDescription().replaceAll("( +)", " ").trim());
            if (!unitNewDto.getDescription().equals(unit.getDescription())) {
                unit.setDescription(unitNewDto.getDescription());
                if (!Objects.isNull(notification)) {
                    notification = NotificationType.UNIT_UPDATED;
                } else {
                    notification = NotificationType.UNIT_DESCRIPTION_UPDATED;
                }
            } else {
                return ResponseEntity.status(400).body(UNIT_DESCRIPTION_NOT_CHANGED);
            }
        }

        sendUnitChangeNotification(unit, notification);
        return ResponseEntity.ok(unitMapper.convert(unitRepository.save(unit)));

    }


    private void sendUnitChangeNotification(Unit unit, NotificationType notificationType) {
        UnitNotificationDto unitNotificationDto = new UnitNotificationDto();
        unitNotificationDto.setUnitId(unit.getId());
        unitNotificationDto.setNotificationType(notificationType);
        switch(notificationType) {
            case UNIT_NAME_UPDATED :
                unitNotificationDto.setContent("Nazwa koła została zmieniona na \""+unit.getName()+"\".");
                break;
            case UNIT_DESCRIPTION_UPDATED :
                unitNotificationDto.setContent("Opis koła \""+unit.getName()+"\" został zmieniony.");
                break;
            case UNIT_UPDATED:
                unitNotificationDto.setContent("Koło \""+unit.getName()+"\" zostało zaktualizowane.");
                break;
        }
        notificationService.insertUnitMembersNotification(unitNotificationDto);
    }

}
