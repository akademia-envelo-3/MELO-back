package pl.envelo.melo.authorization.employee.dto;

import jakarta.validation.constraints.NotBlank;

public class EmployeeNameDto {
    @NotBlank(message = "first name shouldn't be blank")
    private String firstName;
    @NotBlank(message = "last name shouldn't be blank")
    private String lastName;
}
