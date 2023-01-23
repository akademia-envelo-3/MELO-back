package pl.envelo.melo.domain.location;

import jakarta.persistence.*;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @NotBlank
    @Column(nullable = false)
    private String streetName;
    @NotBlank
    @Column(nullable = false, length = 30)
    private String streetNumber;
    private String apartmentNumber;
    @NotBlank
    @Column(nullable = false,length = 6)
    private String postalCode;
    @NotBlank
    @Column(nullable = false)
    private String city;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Location location)) return false;
        return Objects.equals(getStreetNumber(), location.getStreetNumber()) && Objects.equals(getApartmentNumber(), location.getApartmentNumber()) && Objects.equals(getPostalCode(), location.getPostalCode()) && getStreetName().equals(location.getStreetName()) && getCity().equals(location.getCity());

    }

    @Override
    public int hashCode() {
        return Objects.hash(getStreetName(), getStreetNumber(), getApartmentNumber(), getPostalCode(), getCity());
    }
}
