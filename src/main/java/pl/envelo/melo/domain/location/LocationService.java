package pl.envelo.melo.domain.location;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.envelo.melo.domain.location.dto.LocationDto;
import pl.envelo.melo.exceptions.LocationBadRequestException;

import java.util.List;

/**
 The LocationService class is responsible for handling operations related to Location objects.
 It contains a method for inserting or getting a Location object based on the data provided in a LocationDto object.
 */
@RequiredArgsConstructor
@Service
@Transactional
public class LocationService {

    @Autowired
    LocationRepository locationRepository;

    /**
     Inserts or retrieves a Location object based on the data provided in the LocationDto object.
     If all fields in the LocationDto object are empty, null is returned.
     If any of the required fields in the LocationDto object are empty, a LocationBadRequestException is thrown.
     If a Location object with the same data already exists in the database, that object is returned.
     Otherwise, a new Location object is created, saved to the database, and returned.
     @param locationDto The LocationDto object containing the data for the Location object.
     @return The Location object created or retrieved based on the data in the LocationDto object.
     @throws LocationBadRequestException If any of the required fields in the LocationDto object are empty.
     */
    public Location insertOrGetLocation(LocationDto locationDto) {

        if (locationDto.getStreetName().trim().equals("")
                && locationDto.getStreetNumber().trim().equals("")
                && locationDto.getApartmentNumber().trim().equals("")
                && locationDto.getPostalCode().trim().equals("")
                && locationDto.getCity().trim().equals("")) {
            return null;
        } else if (locationDto.getStreetName().trim().equals("")
                || locationDto.getStreetNumber().trim().equals("")
                || locationDto.getPostalCode().trim().equals("")
                || locationDto.getCity().trim().equals("")) {
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
                && newLocation.getCity().equals("")) {
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
}
