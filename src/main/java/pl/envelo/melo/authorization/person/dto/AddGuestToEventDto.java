package pl.envelo.melo.authorization.person.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class AddGuestToEventDto {

    @NotBlank(message = "first name shouldn't be blank")
    private String firstName;
    @NotBlank(message = "last name shouldn't be blank")
    private String lastName;
    @Email(message = "invalid email")
    private String email;
    private int eventId;

}
