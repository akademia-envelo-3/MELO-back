package pl.envelo.melo.domain.notification.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class UnitNotificationDto {
    private int unitId;
    private int employeeId;
}
