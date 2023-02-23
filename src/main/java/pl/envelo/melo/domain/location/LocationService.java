package pl.envelo.melo.domain.location;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.envelo.melo.domain.location.dto.LocationDto;
import pl.envelo.melo.exceptions.LocationBadRequestException;

import java.util.List;

@RequiredArgsConstructor
@Service
public class LocationService {

    @Autowired
    LocationRepository locationRepository;

    public Location insertNewLocation(LocationDto locationDto) {
        return null;
    }

    public Location insertOrGetLocation(LocationDto locationDto) {

        if (locationDto.getStreetName().trim().equals("")
                && locationDto.getStreetNumber().trim().equals("")
                && locationDto.getApartmentNumber().trim().equals("")
                && locationDto.getPostalCode().trim().equals("")
                && locationDto.getCity().trim().equals("")){
            return null;
        } else if(locationDto.getStreetName().trim().equals("")
                || locationDto.getStreetNumber().trim().equals("")
                || locationDto.getPostalCode().trim().equals("")
                || locationDto.getCity().trim().equals("")){
            throw new LocationBadRequestException(LocationConst.MISSING_LOCATION_DATA);
        }

        Location newLocation = new Location();
        newLocation.setStreetName(locationDto.getStreetName());
        newLocation.setStreetNumber(locationDto.getStreetNumber());
        newLocation.setApartmentNumber(locationDto.getApartmentNumber());
        newLocation.setPostalCode(locationDto.getPostalCode());
        newLocation.setCity(locationDto.getCity());

        if (newLocation.getStreetName().equals("")
                && newLocation.getStreetNumber().equals("")
                && newLocation.getApartmentNumber().equals("")
                && newLocation.getPostalCode().equals("")
                && newLocation.getCity().equals("")){
            return null;
        }

        List<Location> toFindLocations = locationRepository.findAll();
        for (Location location : toFindLocations) {
            if (location.getStreetName().equals(newLocation.getStreetName())
                    && location.getStreetNumber().equals(newLocation.getStreetNumber())
                    && location.getApartmentNumber().equals(newLocation.getApartmentNumber())
                    && location.getPostalCode().equals(newLocation.getPostalCode())
                    && location.getCity().equals(newLocation.getCity())) {
                return location;
            }
        }

        locationRepository.save(newLocation);
        return newLocation;

    }

    public LocationDto getLocation(long id) {
        return null;
    }
}
