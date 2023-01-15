package pl.envelo.melo.mappers;

import org.mapstruct.Mapper;
import pl.envelo.melo.domain.location.Location;
import pl.envelo.melo.domain.location.dto.LocationDto;

@Mapper(componentModel = "spring")
public interface LocationMapper {
    Location convert(LocationDto locationDto);
}
