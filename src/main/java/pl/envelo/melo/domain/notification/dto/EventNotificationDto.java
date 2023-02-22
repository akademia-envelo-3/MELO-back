package pl.envelo.melo.domain.notification.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.envelo.melo.domain.notification.NotificationType;

import static pl.envelo.melo.domain.notification.NotificationConst.INVALID_CONTENT_LENGTH;

@Getter
@Setter
@NoArgsConstructor
public class EventNotificationDto {
    @Size(max = 255, message = INVALID_CONTENT_LENGTH)
    private String content;
    private int employeeId;
    private int eventId;
    @NotNull
    private NotificationType type;
}
