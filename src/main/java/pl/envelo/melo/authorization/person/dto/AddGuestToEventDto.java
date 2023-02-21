package pl.envelo.melo.authorization.person.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.envelo.melo.authorization.AuthConst;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddGuestToEventDto {

    @NotBlank(message = AuthConst.INVALID_FIRST_NAME)
    private String firstName;
    @NotBlank(message = AuthConst.INVALID_LAST_NAME)
    private String lastName;
    @Email(regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}",
            flags = Pattern.Flag.CASE_INSENSITIVE, message = AuthConst.INVALID_MAIL)
    private String email;

}
