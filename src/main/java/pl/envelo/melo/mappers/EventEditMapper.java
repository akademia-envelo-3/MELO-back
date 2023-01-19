package pl.envelo.melo.mappers;

import lombok.AllArgsConstructor;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;
import pl.envelo.melo.authorization.employee.Employee;
import pl.envelo.melo.authorization.employee.EmployeeRepository;
import pl.envelo.melo.domain.event.Event;
import pl.envelo.melo.domain.event.dto.NewEventDto;
import pl.envelo.melo.domain.unit.Unit;
import pl.envelo.melo.domain.unit.UnitRepository;

@Mapper(componentModel = "spring", uses = {HashtagMapper.class})
public abstract class EventEditMapper {
    @Autowired
    EmployeeRepository employeeRepository;
    @Autowired
    UnitRepository unitRepository;

    @Mapping(source = "type", target = "eventType")
    public abstract NewEventDto convert(Event event);

    @AfterMapping
    public void updateResult(@MappingTarget  NewEventDto newEventDto, Event event) {
        newEventDto.setOrganizerId(event.getOrganizer().getId());
        if (event.getInvited() != null) {
            event.getInvited().forEach(e -> newEventDto.getInvitedMembers().add(e.getId()));
        }
        if(event.getUnit()!=null) {
            newEventDto.setUnitId(event.getUnit().getId());
        }
        newEventDto.setCategoryId(event.getCategory().getId());
    }
}
