package pl.envelo.melo.domain.location;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "locations")
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
 //   @Max(value = 255, message = "the length of streetName must not be greater than 255")
    private String streetName;

    @NotNull
 //   @Max(value = 100, message = "streetNumber is greater than world's highest street address number.")
    private String streetNumber;

    //    @NotBlank(message = "apartmentNumber field can't be blank")
 //   @Max(value = 100, message = "apartmentNumber is to big. Really? SBM Validation Error.")
    private String apartmentNumber;

    @NotNull
    private String postalCode;

    @NotNull
 //   @Max(value = 255, message = "the length of city must not be greater than 255")
    private String city;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Location location)) return false;
        return getStreetNumber().equals(location.getStreetNumber())
                && getApartmentNumber().equals(location.getApartmentNumber())
                && getPostalCode().equals(location.getPostalCode())
                && getStreetName().equals(location.getStreetName())
                && getCity().equals(location.getCity());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getStreetName(), getStreetNumber(), getApartmentNumber(), getPostalCode(), getCity());
    }
}
