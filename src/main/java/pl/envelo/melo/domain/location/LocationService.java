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
        return null;
    }
    public LocationDto getLocation(long id) {
        return null;
    }
}
