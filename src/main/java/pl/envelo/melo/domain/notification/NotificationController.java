package pl.envelo.melo.domain.notification;


import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import pl.envelo.melo.domain.notification.dto.NotificationDto;

import java.util.List;

@AllArgsConstructor
public class NotificationController {
    private NotificationService notificationService;

    public ResponseEntity<NotificationDto> showAllNotifications(int employeeId) {
        return null;
    }

    public ResponseEntity<List<NotificationDto>> showNewNotifications(int employeeId) {
        return null;
    }

    public ResponseEntity<?> checkNotification(int notificationId) {
        return null;
    }
}
