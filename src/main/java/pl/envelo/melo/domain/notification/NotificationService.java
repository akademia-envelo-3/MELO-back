package pl.envelo.melo.domain.notification;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import pl.envelo.melo.domain.notification.dto.EventNotificationDto;
import pl.envelo.melo.domain.notification.dto.NotificationDto;
import pl.envelo.melo.domain.notification.dto.RequestNotificationDto;
import pl.envelo.melo.domain.notification.dto.UnitNotificationDto;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class NotificationService {
    private NotificationRepository notificationRepository;

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

    public ResponseEntity<?> setNotificationAsChecked(int notificationId) {
        return null;
    }
}
