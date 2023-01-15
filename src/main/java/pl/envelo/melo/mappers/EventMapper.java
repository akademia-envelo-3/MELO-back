package pl.envelo.melo.mappers;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import pl.envelo.melo.domain.event.Event;
import pl.envelo.melo.domain.event.dto.*;

@Mapper(componentModel = "spring")
public interface EventMapper {
    Event convert(EventToDisplayOnListDto eventToDisplayOnListDto);
    @InheritInverseConfiguration
    EventToDisplayOnListDto convert(Event event);
}
