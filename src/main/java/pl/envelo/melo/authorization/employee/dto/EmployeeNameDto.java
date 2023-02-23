package pl.envelo.melo.authorization.employee.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.envelo.melo.authorization.AuthConst;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeNameDto {
    @NotBlank(message = AuthConst.INVALID_FIRST_NAME)
    private String firstName;
    @NotBlank(message = AuthConst.INVALID_LAST_NAME)
    private String lastName;
}
