package pl.envelo.melo.domain.notification.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.envelo.melo.domain.notification.NotificationType;

@NoArgsConstructor
@Getter
@Setter
public class UnitNotificationDto {
    private int unitId;
    private int employeeId;
    private NotificationType notificationType;
}
