package pl.envelo.melo.mappers;

import org.mapstruct.*;
import pl.envelo.melo.authorization.employee.Employee;
import pl.envelo.melo.authorization.employee.dto.EmployeeNameDto;
import pl.envelo.melo.authorization.person.Person;
import pl.envelo.melo.domain.event.Event;
import pl.envelo.melo.domain.event.dto.EventToDisplayOnListDto;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {

    default EmployeeNameDto convert(Employee employee) {
       EmployeeNameDto employeeNameDto = new EmployeeNameDto();

       employeeNameDto.setFirstName(employee.getUser().getPerson().getFirstName());
       employeeNameDto.setLastName(employee.getUser().getPerson().getLastName());

       return employeeNameDto;
    }


}
