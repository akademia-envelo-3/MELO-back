package pl.envelo.melo.domain.unit.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.envelo.melo.authorization.employee.dto.EmployeeNameDto;
import pl.envelo.melo.domain.event.dto.EventToDisplayOnUnitDetailsList;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class UnitDetailsDto {
    private String name;
    private String description;
    private EmployeeNameDto owner;
    private List<EventToDisplayOnUnitDetailsList> events;
    private List<EmployeeNameDto> members;
    private int memberCount;
}
