package pl.envelo.melo.domain.notification.dto;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class RequestNotificationDto {
    @Size(max = 255, message = "\"reason\" : must not be longer than 255 characters")
    private String reason;
    private int employeeId;
}
