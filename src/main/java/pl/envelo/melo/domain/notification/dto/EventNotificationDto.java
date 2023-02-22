package pl.envelo.melo.domain.notification.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.envelo.melo.domain.notification.NotificationType;

@Getter
@Setter
@NoArgsConstructor
public class EventNotificationDto {
    @Size(max = 255, message = "\"content\" : must not be longer than 255 characters")
    private String content;
    private int employeeId;
    private int eventId;
    @NotNull
    private NotificationType type;
}
