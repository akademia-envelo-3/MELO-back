package pl.envelo.melo.domain.location;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import pl.envelo.melo.domain.location.dto.LocationDto;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class LocationServiceTest {

    @Autowired
    private LocationService locationService;
    @Autowired
    private LocationRepository locationRepository;


    @Test
    void getOrInsertLocationTest(){

        assertEquals(Optional.empty(),locationRepository.findById(1L));

        LocationDto locationDto = new LocationDto();
        locationDto.setCity("Warszawa");
        locationDto.setStreetName("Postepu");
        locationDto.setStreetNumber("9");
        locationDto.setApartmentNumber("12");
        locationDto.setPostalCode("01-666");

        assertEquals(Optional.empty(),locationRepository.findByCity("Warszawa"));

        Location location = locationService.insertOrGetLocation(locationDto);

        assertEquals(location.getCity(),locationDto.getCity());
        assertEquals(location.getStreetName(),locationDto.getStreetName());
        assertEquals(location.getCity(),locationRepository.findByCity("Warszawa").get().getCity());
        assertEquals(location.getStreetName(),locationRepository.findByCity("Warszawa").get().getStreetName());
        assertTrue(locationRepository.findById(1L).get().equals(location));
        assertEquals(Optional.empty(),locationRepository.findById(2L));

    }
}
