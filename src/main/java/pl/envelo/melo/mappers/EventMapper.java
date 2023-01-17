package pl.envelo.melo.mappers;

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
import pl.envelo.melo.domain.event.dto.NewEventDto;

@Mapper(componentModel = "spring", uses = {HashtagMapper.class, EmployeeMapper.class, UnitMapper.class, EmployeeService.class})
public interface EventMapper {
    EventMapper INSTANCE = Mappers.getMapper(EventMapper.class);

//    @Autowired
//    EmployeeRepository employeeRepository;

//    Event toEntity(NewEventDto newEventDto);

    @Mapping(source = "eventType", target = "type")
    @Mapping(source = "unitId", target = "unit")
//    @Mapping(source = "organizerId", target = "organizer")
    Event newEvent(NewEventDto newEventDto);

//    @PostMapping
//    default Event convert(NewEventDto newEventDto, @MappingTarget Event event)  {
//        event.setOrganizer(employeeRepository.getReferenceById(newEventDto.getOrganizerId()));
//        event.setType(newEventDto.getEventType());
//        return event;
//    }



//    @Mapping(source = "employeeId", target = "id")
//    default Event convertNewEventDtoToEvent(NewEventDto newEventDto, @Context EmployeeService employeeService) {
//
//    }


//    event.setOrganizer(employeeRepository.getReferenceById(newEventDto.getOrganizerId()));

}
