package pl.envelo.melo.domain.notification;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.envelo.melo.domain.notification.dto.NotificationDto;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/v1/notifications")
@AllArgsConstructor
@CrossOrigin(origins = "${melo.cors-origin}")
public class NotificationController {
    private NotificationService notificationService;

    @PreAuthorize("hasAuthority(@securityConfiguration.getEmployeeRole())")
    @GetMapping("")
    @Operation(summary = "Retrieve list of notifications",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Retrieve list of notifications for user that is executing this method", content =
                    @Content(mediaType = "application/json", schema =
                    @Schema(implementation = NotificationDto.class))),
                    @ApiResponse(responseCode = "401", description = "User executing this method is not an Employee"),
                    @ApiResponse(responseCode = "403", description = "Employee connected with this token is not present in database")
            })
    public ResponseEntity<List<NotificationDto>> showAllNotifications(Principal principal) {
        return notificationService.listAllNotification(principal);
    }

    public ResponseEntity<List<NotificationDto>> showNewNotifications(int employeeId) {
        return null;
    }

    @PreAuthorize("hasAuthority(@securityConfiguration.getEmployeeRole())")
    @Operation(summary = "Set notification as checked",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully set notification as checked")
            })
    @GetMapping("{id}/checked")
    public ResponseEntity<?> checkNotification(@PathVariable("id") int notificationId, Principal principal) {
        return notificationService.setNotificationAsChecked(notificationId, principal);
    }
}
