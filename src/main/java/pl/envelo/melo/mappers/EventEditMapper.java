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

    @Mapping(source = "eventType", target = "type")
    public abstract Event convert(NewEventDto newEventDto);

    @AfterMapping
    public void updateResult(NewEventDto newEventDto, @MappingTarget Event event) {
        event.setOrganizer(employeeRepository.getReferenceById(newEventDto.getOrganizerId()));
        //System.out.println(event.getOrganizer().getId());
        if (newEventDto.getInvitedMembers() != null) {
            for (Integer i : newEventDto.getInvitedMembers()) {
                event.getInvited().add(employeeRepository.getReferenceById(i));
            }
        }
        if (newEventDto.getUnitIds() != null) {
            for (Integer i : newEventDto.getUnitIds()) {
                event.getUnits().add(unitRepository.getReferenceById(i));
            }
            for (Unit unit : event.getUnits()) {
                for (Employee employee : unit.getMembers()) {
                    event.getInvited().add(employee);
                }
            }
        }
    }
}
