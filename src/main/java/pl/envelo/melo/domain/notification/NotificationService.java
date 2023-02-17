package pl.envelo.melo.domain.notification;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import pl.envelo.melo.authorization.employee.Employee;
import pl.envelo.melo.authorization.employee.EmployeeRepository;
import pl.envelo.melo.authorization.person.Person;
import pl.envelo.melo.domain.event.Event;
import pl.envelo.melo.domain.event.EventRepository;
import pl.envelo.melo.domain.notification.dto.EventNotificationDto;
import pl.envelo.melo.domain.notification.dto.NotificationDto;
import pl.envelo.melo.domain.notification.dto.RequestNotificationDto;
import pl.envelo.melo.domain.notification.dto.UnitNotificationDto;
import pl.envelo.melo.domain.unit.Unit;
import pl.envelo.melo.domain.unit.UnitRepository;
import pl.envelo.melo.mappers.EventNotificationMapper;
import pl.envelo.melo.mappers.RequestNotificationMapper;
import pl.envelo.melo.mappers.UnitNotificationMapper;

import java.util.List;
import java.util.Optional;

//@Service
@AllArgsConstructor

@RestController
@Tag(name = "Notification Controller")
@RequestMapping("/NOTIFICATION/")
public class NotificationService {
    @Autowired
    private final NotificationRepository notificationRepository;
    private final EmployeeRepository employeeRepository;
    private final EventRepository eventRepository;
    private final UnitRepository unitRepository;

    private final RequestNotificationMapper requestNotificationMapper;
    private final EventNotificationMapper eventNotificationMapper;
    private final UnitNotificationMapper unitNotificationMapper;

    @Transactional
    @PostMapping("insertEventUpdateNotification")
    public ResponseEntity<?> insertEventUpdateNotification(@RequestBody EventNotificationDto eventNotificationDto) {
        if (eventRepository.findById(eventNotificationDto.getEventId()).isEmpty())
            return ResponseEntity.status(404).body("Event with given ID does not exist");

        Notification notification = eventNotificationMapper.toEntity(eventNotificationDto);
        Event event = eventRepository.findById(eventNotificationDto.getEventId()).get();
        notification.setEvent(event);
        for (Person person : event.getMembers()) {
            Optional<Employee> employeeOptional = employeeRepository.findByUserPersonEmail(person.getEmail());
            if (employeeOptional.isPresent()) {
                Employee employee = employeeOptional.get();
                notification = notificationRepository.save(notification);
                employee.getNotificationsBox().add(notification);
                employeeRepository.save(employee);
            }
        }
        return ResponseEntity.ok().build();
    }
    @PostMapping("insertCreateEventInviteNotification")
    @Transactional
    public ResponseEntity<?> insertCreateEventInviteNotification(@RequestBody EventNotificationDto eventNotificationDto) {
        Notification notification = eventNotificationMapper.toEntity(eventNotificationDto);
        notification.setEvent(eventRepository.findById(eventNotificationDto.getEventId()).get());
        for (Employee employee : notification.getEvent().getInvited()) {
            notification = notificationRepository.save(notification);
            employee.getNotificationsBox().add(notification);
            employeeRepository.save(employee);
        }
        return ResponseEntity.ok().build();
    }
    @PostMapping("insertEventInviteNotification")
    @Transactional
    public ResponseEntity<?> insertEventInviteNotification(@RequestBody  EventNotificationDto eventNotificationDto) {
        Notification notification = eventNotificationMapper.toEntity(eventNotificationDto);
        notification = notificationRepository.save(notification);
        Employee employee = employeeRepository.findById(eventNotificationDto.getEmployeeId()).get();
        employee.getNotificationsBox().add(notification);
        return ResponseEntity.ok().body(employeeRepository.save(employee));
    }
    @PostMapping("insertRequestNotification")
    @Transactional
    public ResponseEntity<?> insertRequestNotification(@RequestBody RequestNotificationDto requestNotificationDto) {
        Employee employee;
        if (employeeRepository.findById(requestNotificationDto.getEmployeeId()).isPresent()) {
            employee = employeeRepository.findById(requestNotificationDto.getEmployeeId()).get();
        } else
            return ResponseEntity.status(404).body("Employee with given ID is not present in database");

        Notification notification = notificationRepository.save(requestNotificationMapper.toEntity(requestNotificationDto));
        employee.getNotificationsBox().add(notification);
        employeeRepository.save(employee);
        return ResponseEntity.ok("Notification has been sent to employee ID: " + employee.getId());
    }
    @PostMapping("insertUnitUpdateNotification")

    @Transactional
    public ResponseEntity<?> insertUnitUpdateNotification(@RequestBody UnitNotificationDto unitNotificationDto) {
        if (unitRepository.findById(unitNotificationDto.getUnitId()).isEmpty())
            return ResponseEntity.status(404).body("Unit with given ID does not exist");

        Notification notification = unitNotificationMapper.toEntity(unitNotificationDto);
        Unit unit = unitRepository.findById(unitNotificationDto.getUnitId()).get();
        notification.setUnit(unit);
        for (Employee employee : unit.getMembers()) {
            notification = notificationRepository.save(notification);
            employee.getNotificationsBox().add(notification);
            employeeRepository.save(employee);
        }
        return ResponseEntity.ok().build();
    }
    @PostMapping("insertUnitOwnerChangeNotification")

    @Transactional
    public ResponseEntity<?> insertUnitOwnerChangeNotification(@RequestBody UnitNotificationDto unitNotificationDto) {
        Notification notification = unitNotificationMapper.toEntity(unitNotificationDto);
        Unit unit = unitRepository.findById(unitNotificationDto.getUnitId()).get();
        notification.setUnit(unit);
        notification = notificationRepository.save(notification);
        Employee employee = employeeRepository.findById(unitNotificationDto.getEmployeeId()).get();
        employee.getNotificationsBox().add(notification);
        employeeRepository.save(employee);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<List<NotificationDto>> listAllNotification(int employeeId) {
        return null;
    }

    public ResponseEntity<List<NotificationDto>> listUncheckedNotification(int employeeId) {
        return null;
    }

    public ResponseEntity<?> setNotificationAsChecked(int notificationId) {
        return null;
    }
}
