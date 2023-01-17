package pl.envelo.melo.domain.location;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Location {
    @Id
    @GeneratedValue
    private int id;
    private String streetName;
    private int streetNumber;
    private int apartmentNumber;
    private String postalCode;
    private String city;

}
