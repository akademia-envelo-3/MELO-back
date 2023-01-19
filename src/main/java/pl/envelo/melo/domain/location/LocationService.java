package pl.envelo.melo.domain.location;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.envelo.melo.domain.location.dto.LocationDto;

@RequiredArgsConstructor
@Service
public class LocationService {

    @Autowired
    LocationRepository locationRepository;

    public Location insertNewLocation(LocationDto locationDto) {
        return null;
    }
    public Location insertOrGetLocation(LocationDto locationDto) {
        Location newLocation = new Location();
        newLocation.setStreetName(locationDto.getStreetName());
        newLocation.setStreetName(locationDto.getStreetNumber());
        newLocation.setApartmentNumber(locationDto.getApartmentNumber());
        newLocation.setPostalCode(locationDto.getPostalCode());
        newLocation.setCity(locationDto.getCity());
        if (locationRepository.findByStreetName(newLocation.getStreetName()).isPresent()){



        } else {
            locationRepository.save(newLocation);
        }
        return newLocation;

    }
    public LocationDto getLocation(long id) {
        return null;
    }
}
