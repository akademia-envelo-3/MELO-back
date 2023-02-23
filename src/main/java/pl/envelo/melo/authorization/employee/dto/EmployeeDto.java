package pl.envelo.melo.authorization.employee.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import pl.envelo.melo.authorization.AuthConst;
import pl.envelo.melo.domain.event.Event;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeDto {

    @NotBlank(message = AuthConst.INVALID_FIRST_NAME)
    private String firstName;
    @NotBlank(message = AuthConst.INVALID_LAST_NAME)
    private String lastName;
    @Email(message = AuthConst.INVALID_MAIL)
    private String email;
    private Set<Event> ownedEvents;
}
