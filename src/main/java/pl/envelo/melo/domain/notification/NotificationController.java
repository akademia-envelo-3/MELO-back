package pl.envelo.melo.domain.notification;


import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.envelo.melo.domain.notification.dto.NotificationDto;

import java.security.Principal;
import java.util.List;

@RequestMapping("/v1/notifications")
@AllArgsConstructor
public class NotificationController {
    private NotificationService notificationService;

    public ResponseEntity<NotificationDto> showAllNotifications(int employeeId) {
        return null;
    }

    public ResponseEntity<List<NotificationDto>> showNewNotifications(int employeeId) {
        return null;
    }
    @PreAuthorize("hasAuthority(@securityConfiguration.getEmployeeRole())")
    @GetMapping("{id}/checked")
    public ResponseEntity<?> checkNotification(int notificationId, Principal principal) {
        return notificationService.setNotificationAsChecked(notificationId, principal);
    }
}
