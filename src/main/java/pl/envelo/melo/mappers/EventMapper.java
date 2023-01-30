package pl.envelo.melo.mappers;

import org.mapstruct.*;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import pl.envelo.melo.authorization.employee.EmployeeRepository;
import pl.envelo.melo.authorization.employee.EmployeeService;
import pl.envelo.melo.authorization.employee.dto.EmployeeDto;
import pl.envelo.melo.domain.event.Event;
import pl.envelo.melo.domain.event.dto.*;
import pl.envelo.melo.domain.event.dto.NewEventDto;

@Mapper(componentModel = "spring", uses = {HashtagMapper.class, EmployeeMapper.class, UnitMapper.class, EmployeeService.class, LocationMapper.class})
public interface EventMapper {
    EventMapper INSTANCE = Mappers.getMapper(EventMapper.class);


    @Mapping(source = "eventType", target = "type")
    @Mapping(source = "unitId", target = "unit")
//    @Mapping(source = "organizerId", target = "organizer")
    Event newEvent(NewEventDto newEventDto);

    @Mapping(ignore = true, target = "category")
    EventToDisplayOnUnitDetailsList convertToEventToDisplayOnUnitDetailsList(Event event);
    @AfterMapping
    default void updateResult(Event event, @MappingTarget EventToDisplayOnUnitDetailsList eventToDisplayOnUnitDetailsList){
        if(event.getCategory() != null)
            eventToDisplayOnUnitDetailsList.setCategory(event.getCategory().getName());
        eventToDisplayOnUnitDetailsList.setEventId(event.getId());
    }

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
