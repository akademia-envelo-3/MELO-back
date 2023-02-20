package pl.envelo.melo.domain.notification;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
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

@Service
@AllArgsConstructor
public class NotificationService {
    @Autowired
    private final NotificationRepository notificationRepository;
    private final EmployeeRepository employeeRepository;
    private final EventRepository eventRepository;
    private final UnitRepository unitRepository;

    private final RequestNotificationMapper requestNotificationMapper;
    private final EventNotificationMapper eventNotificationMapper;
    private final UnitNotificationMapper unitNotificationMapper;


    public ResponseEntity<?> insertEventAllMembersNotification(EventNotificationDto eventNotificationDto) {
        //used in update event, for members: employee and guest (person)
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
            } else {
                //todo send email
            }
        }
        return ResponseEntity.ok().body("Notifications have been sent to " + event.getMembers().size() + " members.");
    }

    public ResponseEntity<?> insertEventInvitedNotification(EventNotificationDto eventNotificationDto) {
        //used in create event
        Notification notification = eventNotificationMapper.toEntity(eventNotificationDto);
        notification.setEvent(eventRepository.findById(eventNotificationDto.getEventId()).get());
        for (Employee employee : notification.getEvent().getInvited()) {
            notification = notificationRepository.save(notification);
            employee.getNotificationsBox().add(notification);
            employeeRepository.save(employee);
        }
        return ResponseEntity.ok().body("Notifications have been sent to " + notification.getEvent().getInvited().size() + " invited people.");
    }

    public ResponseEntity<?> insertEventEmployeeMembersNotification(EventNotificationDto eventNotificationDto) {
        if (eventRepository.findById(eventNotificationDto.getEventId()).isEmpty())
            return ResponseEntity.status(404).body("Event with given ID does not exist");

        Notification notification = eventNotificationMapper.toEntity(eventNotificationDto);
        Event event = eventRepository.findById(eventNotificationDto.getEventId()).get();
        notification.setEvent(event);
        int employeeSize = 0;
        for (Person person : event.getMembers()) {
            Optional<Employee> employeeOptional = employeeRepository.findByUserPersonEmail(person.getEmail());
            if (employeeOptional.isPresent()) {
                Employee employee = employeeOptional.get();
                notification = notificationRepository.save(notification);
                employee.getNotificationsBox().add(notification);
                employeeRepository.save(employee);
                employeeSize++;
            }
        }
        return ResponseEntity.ok().body("Notifications have been sent to " + employeeSize + " members.");
    }

    //redundant - method to invite one person out of create/update event
    public ResponseEntity<?> insertEventInviteNotification(EventNotificationDto eventNotificationDto) {
        Notification notification = eventNotificationMapper.toEntity(eventNotificationDto);
        notification = notificationRepository.save(notification);
        Employee employee = employeeRepository.findById(eventNotificationDto.getEmployeeId()).get();
        employee.getNotificationsBox().add(notification);
        return ResponseEntity.ok().body(employeeRepository.save(employee));
    }

    public ResponseEntity<?> insertRequestNotification(RequestNotificationDto requestNotificationDto) {
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

    public ResponseEntity<?> insertUnitMembersNotification(UnitNotificationDto unitNotificationDto) {
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
        return ResponseEntity.ok().body("Sent " + unitNotificationDto.getNotificationType() + " to " + unit.getMembers().size() + "unit members.");
    }

    public ResponseEntity<?> insertUnitOwnerChangeNotification(UnitNotificationDto unitNotificationDto) {
        Notification notification = unitNotificationMapper.toEntity(unitNotificationDto);
        Unit unit = unitRepository.findById(unitNotificationDto.getUnitId()).get();
        notification.setUnit(unit);
        notification = notificationRepository.save(notification);
        Employee employee = employeeRepository.findById(unitNotificationDto.getEmployeeId()).get();
        employee.getNotificationsBox().add(notification);
        employeeRepository.save(employee);
        return ResponseEntity.ok().body("Added new notification for employee with ID " + employee.getId() +
                ": " + notification.getNotificationType());
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
