package pl.envelo.melo.domain.notification;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.envelo.melo.domain.notification.dto.NotificationDto;
import pl.envelo.melo.domain.unit.dto.UnitToDisplayOnListDto;

import java.security.Principal;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("v1/notifications")
public class NotificationController {
    private NotificationService notificationService;

    @PreAuthorize("hasAuthority(@securityConfiguration.getEmployeeRole())")
    @GetMapping("")
    @Operation(summary = "Retrieve list of notifications",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Retrieve list of notifications for user that is executing this method", content =
                    @Content(mediaType = "application/json", schema =
                    @Schema(implementation = NotificationDto.class)))
            })
    public ResponseEntity<List<NotificationDto>> showAllNotifications(Principal principal) {
        return notificationService.listAllNotification(principal);
    }

    public ResponseEntity<List<NotificationDto>> showNewNotifications(int employeeId) {
        return null;
    }

    public ResponseEntity<?> checkNotification(int notificationId) {
        return null;
    }
}
