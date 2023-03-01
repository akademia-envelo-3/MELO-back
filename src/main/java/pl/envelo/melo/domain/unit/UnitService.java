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

/**
 The UnitService class is responsible for managing and manipulating units, which are groups of employees.
 This class includes methods for getting a single unit by ID or multiple units by name or description,
 changing ownership of a unit, and adding an employee to a unit.
 The class utilizes the UnitRepository and EmployeeRepository interfaces for accessing the data.
 */
@Service
@AllArgsConstructor
@Transactional
public class UnitService {

    private final UnitRepository unitRepository;
    private final EmployeeRepository employeeRepository;
    private final AdminRepository adminRepository;
    private final EmployeeService employeeService;
    private final UnitMapper unitMapper;
    private final UnitDetailsMapper unitDetailsMapper;
    private final NotificationService notificationService;
    private final AuthorizationService authorizationService;


    /**
     Returns a ResponseEntity object containing a UnitDetailsDTO object for the specified unit ID.
     If the unit does not exist, a ResponseEntity object with a status code of 404 and an error message is returned.
     @param id: an integer representing the ID of the unit to retrieve
     @return a ResponseEntity object containing a UnitDetailsDTO object for the specified unit ID
     */
    public ResponseEntity<?> getUnit(int id) {
        Optional<Unit> unit = unitRepository.findById(id);
        if (unit.isPresent()) {
            return ResponseEntity.ok(unitDetailsMapper.convert(unit.get()));
        }
        return ResponseEntity.status(404).body(UNIT_DOES_NOT_EXIST);
    }

    /**
     Returns a ResponseEntity object containing a list of UnitDTO objects that match the specified text string.
     If the text string is null or empty, all units are returned. The list is sorted in descending order by the number of unit members.
     @param text: a String representing the text to search for in unit names and descriptions
     @return a ResponseEntity object containing a list of UnitDTO objects that match the specified text string
     */
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

    /**
     This method is used to change ownership of a unit to a new employee.
     @param newEmployeeId ID of the new employee who will be the new owner of the unit
     @param unitId ID of the unit whose ownership needs to be changed
     @param principal A security principal representing the authenticated user
     @return ResponseEntity A response entity containing the result of the operation
     */
    public ResponseEntity<?> changeOwnership(int newEmployeeId, int unitId, Principal principal) {
        Employee oldOwner = employeeRepository.findByUserId(authorizationService.getUUID(principal)).orElse(null);
        Admin admin = adminRepository.findByUserId(authorizationService.getUUID(principal)).orElse(null);
        if (Objects.nonNull(admin))
            return changeOwnershipByAdmin(unitId, newEmployeeId);
        if (Objects.nonNull(oldOwner))
            return changeOwnershipByEmployee(newEmployeeId, unitId, oldOwner);
        return ResponseEntity.status(403).build();
    }

    /**
     Changes the ownership of a unit from the current owner to a new employee, if the current user is an employee.
     If the current user is an admin, this method will return a 403 Forbidden response.
     If the unit or the new owner is not found, this method will return a 404 Not Found response.
     If the current owner is not authorized to change ownership of the unit, this method will return a 400 Bad Request response.
     If the new owner is the same as the current owner, this method will return a 400 Bad Request response.
     @param newEmployeeId the ID of the new employee who will become the owner of the unit
     @param unitId the ID of the unit to change ownership for
     @param oldOwner the current owner of the unit
     @return a ResponseEntity with the updated unit and a 200 OK response, or an error response if any errors occur
     */
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

    /**
     Returns a ResponseEntity indicating that the ownership of a unit has been successfully changed to a new owner.
     @param unitId the ID of the unit for which ownership has been changed
     @param newOwner the new owner of the unit
     @return a ResponseEntity with a 200 status code and a success message indicating the ID of the unit and the full name of the new owner
     */
    private ResponseEntity<?> unitOwnershipChanged(int unitId, Employee newOwner) {
        return ResponseEntity.status(200).body("The owner of the unit with id "
                + unitId + " has been correctly changed to "
                + newOwner.getUser().getPerson().getFirstName() + " "
                + newOwner.getUser().getPerson().getLastName());
    }

    /**
     This method changes the ownership of a unit to a new owner. The method is only accessible to admins and requires the ID of the unit and the ID of the new owner as input parameters. If the unit or the new owner cannot be found, the method will return an HTTP 404 response with an error message. If the new owner is already the owner of the unit, the method will return an HTTP 400 response with an error message. Otherwise, the method will change the ownership of the unit to the new owner, remove the unit from the owned units of the previous owner, add the unit to the owned and joined units of the new owner, add the new owner as a member of the unit, and send notifications to both the previous and new owners. The method will return an HTTP 200 response with a success message if the ownership change is successful.
     @param unitId The ID of the unit whose ownership is to be changed.
     @param nextOwnerId The ID of the employee who will become the new owner of the unit.
     @return An HTTP response with a success message if the ownership change is successful, or an error message if there is an issue with the input parameters.
     */
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

    /**
     Adds an employee as a member of a given unit.
     @param employee the employee to add as a member
     @param unit the unit to add the employee to
     @return true if the employee was added as a member of the unit, false otherwise
     */
    private boolean addMemberToUnit(Employee employee, Unit unit) {
        if (Objects.isNull(employee) || Objects.isNull(unit))
            return false;
        if (Objects.isNull(unit.getMembers()))
            unit.setMembers(new HashSet<>());
        return unit.getMembers().add(employee);
    }

    /**
     Sends a notification to the affected employee when unit ownership is changed.
     @param employeeId The id of the affected employee.
     @param unitId The id of the unit.
     @param revoke A boolean value indicating whether the employee's ownership of the unit has been revoked.
     */
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

    /**
     This method adds an employee to a unit.
     @param unitId the ID of the unit the employee is to be added to.
     @param principal the principal object that represents the authenticated user.
     @return ResponseEntity that represents the HTTP response to the client.
     If the operation is successful, it returns ResponseEntity with HTTP status 200 and a boolean true.
     If the unit does not exist, it returns ResponseEntity with HTTP status 404 and a String message "UNIT_DOES_NOT_EXIST".
     If the employee is already a member of the unit, it returns ResponseEntity with HTTP status 400 and a String message "EMPLOYEE_ALREADY_IN_UNIT".
     @throws EmployeeNotFoundException if the employee cannot be found in the database.
     */
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

    /**
     Allows an employee to leave a unit.
     @param unitId the ID of the unit the employee wants to leave.
     @param principal the Principal object representing the currently authenticated user.
     @return a ResponseEntity containing a boolean value indicating whether the operation was successful or an error message if it failed.
     @throws EmployeeNotFoundException if the employee associated with the current user ID is not found in the database.
     */
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
        return ResponseEntity.status(404).body(UNIT_DOES_NOT_EXIST);
    }

    private ResponseEntity<?> employeeSuccessfulLeave(int employeeId) {
        return ResponseEntity.ok("Employee with ID " + employeeId +
                " was correctly removed from the members of the unit");
    }

    /**
     Inserts a new unit into the system and assigns it to the currently logged in employee
     @param unitNewDto DTO object containing the new unit's details
     @param principal the currently logged in user's security context
     @return a ResponseEntity object containing a UnitToDisplayOnListDto object representing the newly created unit, or a HTTP error status with an appropriate error message
     @throws EmployeeNotFoundException if the currently logged in user is not found in the system
     */
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


    /**
     Updates the specified unit with new information provided in the UnitNewDto object.
     If the specified unit is not found, it returns a 404 NOT FOUND response.
     If the user is not authorized to update the specified unit, it returns a 403 FORBIDDEN response.
     If the unit name or description is not changed, it returns a 400 BAD REQUEST response.
     If the update is successful, it sends a unit change notification and returns a 200 OK response with the updated unit object.
     @param unitId the ID of the unit to be updated
     @param unitNewDto the UnitNewDto object containing new information for the unit
     @param principal the Principal object representing the user making the request
     @return a ResponseEntity object with a 200 OK response and the updated unit object on success,
     or a 400 BAD REQUEST response if the unit name or description is not changed,
     or a 403 FORBIDDEN response if the user is not authorized to update the specified unit,
     or a 404 NOT FOUND response if the specified unit is not found
     @throws EmployeeNotFoundException if the employee is not found
     */
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

    /**
     Sends a notification to the members of a unit when the unit is updated.
     @param unit The unit that was updated.
     @param notificationType The type of the notification to be sent.
     */
    private void sendUnitChangeNotification(Unit unit, NotificationType notificationType) {
        UnitNotificationDto unitNotificationDto = new UnitNotificationDto();
        unitNotificationDto.setUnitId(unit.getId());
        unitNotificationDto.setNotificationType(notificationType);
        switch (notificationType) {
            case UNIT_NAME_UPDATED:
                unitNotificationDto.setContent("Nazwa koła została zmieniona na \"" + unit.getName() + "\".");
                break;
            case UNIT_DESCRIPTION_UPDATED:
                unitNotificationDto.setContent("Opis koła \"" + unit.getName() + "\" został zmieniony.");
                break;
            case UNIT_UPDATED:
                unitNotificationDto.setContent("Koło \"" + unit.getName() + "\" zostało zaktualizowane.");
                break;
        }
        notificationService.insertUnitMembersNotification(unitNotificationDto);
    }

}
