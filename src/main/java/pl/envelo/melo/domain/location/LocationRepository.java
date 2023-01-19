package pl.envelo.melo.domain.location;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LocationRepository extends JpaRepository<Location, Long> {


    Optional<Location> findByStreetName(String streetName);
//    Optional<Location> findByStreetNameByStreetNumberByApartmentNumberByPostalCodeByCity(
//            String streetName,String streetNumber,String apartmentNumber, String postalCode, String city
//    );

}
