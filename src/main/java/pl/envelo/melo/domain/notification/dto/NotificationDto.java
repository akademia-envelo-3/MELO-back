package pl.envelo.melo.domain.notification.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.envelo.melo.domain.notification.NotificationType;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
public class NotificationDto {
    private int id;
    private String content;
    private String eventName;
    private String unitName;
    @NotNull(message = "\"notificationType\" : must not be null")
    private NotificationType notificationType;
    private LocalDateTime timestamp;
    private boolean checked;
}
