package pl.envelo.melo.domain.unit;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.envelo.melo.authorization.employee.Employee;
import pl.envelo.melo.authorization.employee.EmployeeRepository;
import pl.envelo.melo.authorization.employee.EmployeeService;
import pl.envelo.melo.domain.notification.NotificationService;
import pl.envelo.melo.domain.notification.NotificationType;
import pl.envelo.melo.domain.notification.dto.UnitNotificationDto;
import pl.envelo.melo.domain.unit.dto.UnitToDisplayOnListDto;
import pl.envelo.melo.domain.unit.dto.UnitNewDto;
import pl.envelo.melo.mappers.UnitDetailsMapper;
import pl.envelo.melo.mappers.UnitMapper;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UnitService {

    private final UnitRepository unitRepository;
    private final EmployeeRepository employeeRepository;
    private final EmployeeService employeeService;
    private final UnitMapper unitMapper;
    private final UnitDetailsMapper unitDetailsMapper;
    private final NotificationService notificationService;

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

    public ResponseEntity<?> changeOwnershipByAdmin(int unitId, int nextOwnerId) {
        Optional<Unit> unit = unitRepository.findById(unitId);
        Optional<Employee> nextOwner = employeeRepository.findById(nextOwnerId);
        if(unit.isEmpty() || nextOwner.isEmpty())
            return ResponseEntity.status(404).body("Atleast one of the provided entity ids does not exist in the database");
        if(unit.get().getOwner().getId() == nextOwnerId)
            return ResponseEntity.ok().build();
        Employee currentOwner = unit.get().getOwner();
        unit.get().setOwner(nextOwner.get());
        employeeService.removeFromOwnedUnits(currentOwner.getId(), unit.get());
        employeeService.addToOwnedUnits(nextOwnerId, unit.get());
        employeeService.addToJoinedUnits(nextOwnerId,unit.get());
        addMemberToUnit(nextOwner.get(), unit.get());
        unitRepository.save(unit.get());
        sendOwnershipNotification(currentOwner.getId(),unit.get().getId(), true);
        sendOwnershipNotification(nextOwnerId,unit.get().getId(), false);
        return ResponseEntity.ok().build();
    }
    private boolean addMemberToUnit(Employee employee, Unit unit){
        if(Objects.isNull(employee) || Objects.isNull(unit))
            return false;
        if(Objects.isNull(unit.getMembers()))
            unit.setMembers(new HashSet<>());
        return unit.getMembers().add(employee);
    }
    private void sendOwnershipNotification(int employeeId, int unitId, boolean revoke){
        UnitNotificationDto unitNotificationDto = new UnitNotificationDto();
        unitNotificationDto.setUnitId(unitId);
        unitNotificationDto.setEmployeeId(employeeId);
        if(revoke)
            unitNotificationDto.setNotificationType(NotificationType.UNIT_OWNERSHIP_REVOKED);
        else
            unitNotificationDto.setNotificationType(NotificationType.UNIT_OWNERSHIP_GRANTED);
        notificationService.insertUnitNotification(unitNotificationDto);
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

    public ResponseEntity<?> updateUnit(UnitToDisplayOnListDto unitToDisplayOnListDto) {
        Unit unit;
        if(unitRepository.findById(unitToDisplayOnListDto.getUnitId()).isPresent()) {
            unit = unitRepository.findById(unitToDisplayOnListDto.getUnitId()).get();
        } else {
            return ResponseEntity.status(404).body("Unit with given ID is not present in database");
        }
        NotificationType notification = null;
        if(!unitToDisplayOnListDto.getName().equals(unit.getName()) &&
                !unitToDisplayOnListDto.getDescription().equals(unit.getDescription())) {
            unit.setName(unitToDisplayOnListDto.getName());
            unit.setDescription(unitToDisplayOnListDto.getDescription());
            notification = NotificationType.UNIT_UPDATED;
        }
        if(!unitToDisplayOnListDto.getName().equals(unit.getName())) {
            unit.setName(unitToDisplayOnListDto.getName());
            notification = NotificationType.UNIT_NAME_UPDATED;
        }
        if(!unitToDisplayOnListDto.getDescription().equals(unit.getDescription())) {
            unit.setDescription(unitToDisplayOnListDto.getDescription());
            notification = NotificationType.UNIT_DESCRIPTION_UPDATED;
        }

        if(notification!=null) {
            sendUnitChangeNotification(unit, notification);
            return ResponseEntity.ok(unitMapper.convert(unitRepository.save(unit)));
        }
        return ResponseEntity.status(400).body("Unit name and description in database is the same that you're trying to send.");
    }

    private void sendUnitChangeNotification(Unit unit, NotificationType notificationType){
        if(unit.getMembers().isEmpty()) {
            return;
        }

        UnitNotificationDto unitNotificationDto = new UnitNotificationDto();
        unitNotificationDto.setUnitId(unit.getId());
        unitNotificationDto.setNotificationType(notificationType);


        for(Employee employee : unit.getMembers()) {
            unitNotificationDto.setEmployeeId(employee.getId());
            notificationService.insertUnitNotification(unitNotificationDto);
        }
    }

}
