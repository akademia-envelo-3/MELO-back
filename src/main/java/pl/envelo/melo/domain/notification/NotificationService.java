package pl.envelo.melo.domain.notification;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.envelo.melo.authorization.AuthorizationService;
import pl.envelo.melo.authorization.admin.Admin;
import pl.envelo.melo.authorization.employee.Employee;
import pl.envelo.melo.authorization.employee.EmployeeRepository;
import pl.envelo.melo.domain.notification.dto.EventNotificationDto;
import pl.envelo.melo.domain.notification.dto.NotificationDto;
import pl.envelo.melo.domain.notification.dto.RequestNotificationDto;
import pl.envelo.melo.domain.notification.dto.UnitNotificationDto;
import pl.envelo.melo.mappers.NotificationMapper;

import java.security.Principal;
import java.util.*;

@Service
@AllArgsConstructor
public class NotificationService {
    private NotificationRepository notificationRepository;

    @Autowired
    private final AuthorizationService authorizationService;
    private final EmployeeRepository employeeRepository;
    private final NotificationMapper notificationMapper;

    public ResponseEntity<EventNotificationDto> insertEventNotification(EventNotificationDto eventNotificationDto) {
        return null;
    }

    public ResponseEntity<RequestNotificationDto> insertRequestNotification(RequestNotificationDto requestNotificationDto) {
        return null;
    }

    public ResponseEntity<UnitNotificationDto> insertUnitNotification(UnitNotificationDto unitNotificationDto) {
        return null;
    }

    public ResponseEntity<List<NotificationDto>> listAllNotification(Principal principal) {
        authorizationService.inflateUser(principal);
        Employee employee = employeeRepository.findByUserId(authorizationService.getUUID(principal)).orElse(null);
        if(Objects.nonNull(employee)) {
            if(Objects.isNull(employee.getNotificationsBox())) {
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

    public ResponseEntity<?> setNotificationAsChecked(int notificationId) {
        return null;
    }
}
