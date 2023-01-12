package pl.envelo.melo.mappers;

import org.mapstruct.*;
import pl.envelo.melo.domain.event.Event;
import pl.envelo.melo.domain.event.dto.*;

@Mapper(componentModel = "spring")
public interface EventMapper {
    @Mapping(source = "id", target = "eventId")
    public EventToDisplayOnListDto convert(Event event);
    @AfterMapping
    default void updateResult(Event event, @MappingTarget EventToDisplayOnListDto eventToDisplayOnListDto){
        int members = 0;
        if(event.getMembers() != null)
            members = event.getMembers().size();
        eventToDisplayOnListDto.setInvitedMembersNumber(members);
    }
}