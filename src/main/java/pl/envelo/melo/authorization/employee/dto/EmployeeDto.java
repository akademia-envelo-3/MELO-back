package pl.envelo.melo.authorization.employee.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import pl.envelo.melo.domain.event.Event;

import java.util.Set;

public class EmployeeDto {

    @NotBlank(message = "first name shouldn't be blank")
    private String firstName;
    @NotBlank(message = "last name shouldn't be blank")
    private String lastName;
    @Email(message = "invalid email")
    private String email;
    private Set<Event> ownedEvents;
}
