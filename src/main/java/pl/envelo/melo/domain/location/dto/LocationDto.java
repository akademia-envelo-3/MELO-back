package pl.envelo.melo.domain.location.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LocationDto {

    @NotBlank(message = "streetName field can't be blank")
    @Max(value = 255, message = "the length of streetName must not be greater than 255")
    private String streetName;

    @NotBlank(message = "streetNumber field can't be blank")
    @Max(value = 986039, message = "streetNumber is greater than world's highest street address number.")
    private int streetNumber;

    @NotBlank(message = "apartmentNumber field can't be blank")
    @Max(value = 2115, message = "apartmentNumber is bigger than 2115. Really? SBM Validation Error.")
    private int apartmentNumber;

    @NotBlank(message = "postalCode field can't be blank")
    @Digits(integer = 5, fraction = 0, message = "postalCode must have 5 digits")
    private int postalCode;

    @NotBlank(message = "city field can't be blank")
    @Max(value = 255, message = "the length of city must not be greater than 255")
    private String city;

}
