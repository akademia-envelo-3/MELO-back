package pl.envelo.melo.domain.notification;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.envelo.melo.authorization.AuthorizationService;
import pl.envelo.melo.authorization.employee.Employee;
import pl.envelo.melo.authorization.employee.EmployeeRepository;
import pl.envelo.melo.domain.notification.dto.EventNotificationDto;
import pl.envelo.melo.domain.notification.dto.NotificationDto;
import pl.envelo.melo.domain.notification.dto.RequestNotificationDto;
import pl.envelo.melo.domain.notification.dto.UnitNotificationDto;
import pl.envelo.melo.exceptions.EmployeeNotFoundException;
import pl.envelo.melo.exceptions.NotificationNotFoundException;
import pl.envelo.melo.mappers.NotificationMapper;
import pl.envelo.melo.mappers.RequestNotificationMapper;

import java.security.Principal;
import java.util.*;

@Service
@AllArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final AuthorizationService authorizationService;
    private final EmployeeRepository employeeRepository;
    private final NotificationMapper notificationMapper;
    private final RequestNotificationMapper requestNotificationMapper;

    public ResponseEntity<EventNotificationDto> insertEventNotification(EventNotificationDto eventNotificationDto) {
        return null;
    }

    public ResponseEntity<?> insertRequestNotification(RequestNotificationDto requestNotificationDto) {
        Employee employee;
        if (employeeRepository.findById(requestNotificationDto.getEmployeeId()).isPresent()) {
            employee = employeeRepository.findById(requestNotificationDto.getEmployeeId()).get();
        } else
            throw new EmployeeNotFoundException();

        Notification notification = requestNotificationMapper.toEntity(requestNotificationDto);
        saveNotificationAndEmployee(notification, employee);
        return ResponseEntity.ok("Notification " + notification.getNotificationType() + " has been sent to employee ID: " + employee.getId());
    }

    private void saveNotificationAndEmployee(Notification notification, Employee employee) {
        notification = notificationRepository.save(notification);
        employee.getNotificationsBox().add(notification);
        employeeRepository.save(employee);
    }

    public ResponseEntity<UnitNotificationDto> insertUnitNotification(UnitNotificationDto unitNotificationDto) {
        return null;
    }

    public ResponseEntity<List<NotificationDto>> listAllNotification(Principal principal) {
        authorizationService.inflateUser(principal);
        Employee employee = employeeRepository.findByUserId(authorizationService.getUUID(principal)).orElse(null);
        if (Objects.nonNull(employee)) {
            if (Objects.isNull(employee.getNotificationsBox())) {
                return ResponseEntity.ok(new ArrayList<>());
            }
            List<Notification> checkedNotifications = new ArrayList<>(employee.getNotificationsBox().stream().filter(Notification::isChecked).toList());
            List<Notification> notifications = new ArrayList<>(employee.getNotificationsBox().stream().filter(notification -> !notification.isChecked()).toList());
            checkedNotifications.sort(Comparator.comparing(Notification::getTimestamp).reversed());
            notifications.sort(Comparator.comparing(Notification::getTimestamp).reversed());
            notifications.addAll(checkedNotifications);
            return ResponseEntity.ok(notificationMapper.toDto(notifications));
        } else {
            return ResponseEntity.status(403).build();
        }
    }

    public ResponseEntity<List<NotificationDto>> listUncheckedNotification(int employeeId) {
        return null;
    }

    public ResponseEntity<?> setNotificationAsChecked(int notificationId, Principal principal) {
        Employee employee = employeeRepository.findByUserId(authorizationService.getUUID(principal)).orElseThrow(EmployeeNotFoundException::new);
        Notification notification = notificationRepository.findById(notificationId).orElseThrow(NotificationNotFoundException::new);
        Set<Notification> notificationsBox = employee.getNotificationsBox();
        if (notificationsBox.contains(notification)) {
            notification.setChecked(true);
            notificationRepository.save(notification);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(403).build();
    }
}
