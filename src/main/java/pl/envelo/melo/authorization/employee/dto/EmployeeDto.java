package pl.envelo.melo.authorization.employee.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.envelo.melo.domain.event.Event;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeDto {

    @NotBlank(message = "first name shouldn't be blank")
    private String firstName;
    @NotBlank(message = "last name shouldn't be blank")
    private String lastName;
    @Email(message = "invalid email")
    private String email;
    private Set<Event> ownedEvents;
}
