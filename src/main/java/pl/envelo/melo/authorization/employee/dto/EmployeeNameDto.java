package pl.envelo.melo.authorization.employee.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeNameDto {

    @NotBlank(message = "first name shouldn't be blank")
    private String firstName;
    @NotBlank(message = "last name shouldn't be blank")
    private String lastName;
}
