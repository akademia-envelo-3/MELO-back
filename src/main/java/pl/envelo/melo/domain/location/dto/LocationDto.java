package pl.envelo.melo.domain.location.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LocationDto {

    @NotBlank(message = "streetName field can't be blank")
    @Max(value = 255, message = "the length of streetName must not be greater than 255")
    private String streetName;

    @NotBlank(message = "streetNumber field can't be blank")
    @Max(value = 100, message = "streetNumber is greater than world's highest street address number.")
    private String streetNumber;

//    @NotBlank(message = "apartmentNumber field can't be blank")
    @Max(value = 100, message = "apartmentNumber is bigger than 2115. Really? SBM Validation Error.")
    private String apartmentNumber;

    @NotBlank(message = "postalCode field can't be blank")
    @Size(min = 6, max = 6)
    private String postalCode;

    @NotBlank(message = "city field can't be blank")
    @Max(value = 255, message = "the length of city must not be greater than 255")
    private String city;

}
