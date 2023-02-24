package pl.envelo.melo.domain.notification.dto;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.envelo.melo.domain.notification.NotificationType;

import static pl.envelo.melo.domain.notification.NotificationConst.INVALID_REASON_LENGTH;

@NoArgsConstructor
@Getter
@Setter
public class RequestNotificationDto {
    @Size(max = 255, message = INVALID_REASON_LENGTH)
    private String reason;
    private int employeeId;
    private NotificationType notificationType;
}
