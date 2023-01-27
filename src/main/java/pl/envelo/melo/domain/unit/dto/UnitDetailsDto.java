package pl.envelo.melo.domain.unit.dto;

import pl.envelo.melo.authorization.employee.dto.EmployeeNameDto;
import pl.envelo.melo.domain.event.dto.EventToDisplayOnUnitDetailsList;

import java.util.List;

public class UnitDetailsDto {
    String name;
    String description;
    EmployeeNameDto owner;
    List<EventToDisplayOnUnitDetailsList> events;
}
