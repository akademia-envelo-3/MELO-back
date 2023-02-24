package pl.envelo.melo.domain.notification.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
    @Size(max = 255, message = "\"content\" : must not be longer than 255 characters")
    private String content;
    private Integer eventId;
    private Integer unitId;
    @NotNull(message = "\"notificationType\" : must not be null")
    private NotificationType notificationType;
    private LocalDateTime timestamp;
    private boolean checked;
}
