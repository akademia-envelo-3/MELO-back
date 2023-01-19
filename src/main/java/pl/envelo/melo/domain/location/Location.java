package pl.envelo.melo.domain.location;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String streetName;
    private String streetNumber;
    private String apartmentNumber;
    private String postalCode;
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

//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (!(o instanceof Location location)) return false;
//        return getStreetNumber() == location.getStreetNumber()
//                && getApartmentNumber() == location.getApartmentNumber()
//                && getPostalCode() == location.getPostalCode()
//                && getStreetName().equals(location.getStreetName())
//                && getCity().equals(location.getCity());
//    }

    @Override
    public int hashCode() {
        return Objects.hash(getStreetName(), getStreetNumber(), getApartmentNumber(), getPostalCode(), getCity());
    }
}
