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
import pl.envelo.melo.exceptions.EmployeeNotFound;
import pl.envelo.melo.exceptions.NotificationNotFoundException;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@AllArgsConstructor
public class NotificationService {
    private NotificationRepository notificationRepository;
    private AuthorizationService authorizationService;
    private EmployeeRepository employeeRepository;

    public ResponseEntity<EventNotificationDto> insertEventNotification(EventNotificationDto eventNotificationDto) {
        return null;
    }

    public ResponseEntity<RequestNotificationDto> insertRequestNotification(RequestNotificationDto requestNotificationDto) {
        return null;
    }

    public ResponseEntity<UnitNotificationDto> insertUnitNotification(UnitNotificationDto unitNotificationDto) {
        return null;
    }

    public ResponseEntity<List<NotificationDto>> listAllNotification(int employeeId) {
        return null;
    }

    public ResponseEntity<List<NotificationDto>> listUncheckedNotification(int employeeId) {
        return null;
    }

    public ResponseEntity<?> setNotificationAsChecked(int notificationId, Principal principal) {
        Employee employee = employeeRepository.findByUserId(authorizationService.getUUID(principal)).orElseThrow(EmployeeNotFound::new);
        Notification notification = notificationRepository.findById(notificationId).orElseThrow(NotificationNotFoundException::new);
        Set<Notification> notificationsBox = employee.getNotificationsBox();
        if(notificationsBox.contains(notification))
            return ResponseEntity.ok().build();
        return ResponseEntity.status(403).build();
    }
}
