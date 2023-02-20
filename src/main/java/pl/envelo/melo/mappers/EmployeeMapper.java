package pl.envelo.melo.mappers;

import org.mapstruct.*;
import org.mapstruct.Context;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.http.ResponseEntity;
import pl.envelo.melo.authorization.employee.Employee;
import pl.envelo.melo.authorization.employee.EmployeeService;
import pl.envelo.melo.authorization.employee.dto.EmployeeDto;
import pl.envelo.melo.authorization.employee.dto.EmployeeNameDto;
import pl.envelo.melo.authorization.person.Person;
import pl.envelo.melo.domain.event.dto.EventToDisplayOnListDto;
import pl.envelo.melo.domain.event.Event;
import pl.envelo.melo.domain.event.dto.NewEventDto;

@Mapper(componentModel = "spring", uses = { EmployeeService.class })
public interface EmployeeMapper extends EntityMapper<EmployeeDto, Employee>{

//    @Mapping(source = "employeeId", target = "id")
//    default ResponseEntity<EmployeeDto> map(Integer id, @Context EmployeeService employeeService) {
//        return employeeService.getEmployee(id);
//    }

    @Mapping(source = "firstName", target = "user.person.firstName")
    @Mapping(source = "lastName", target = "user.person.lastName")
    @Mapping(source = "email", target = "user.person.email")
    Employee toEntity(EmployeeDto employeeDto);
    @InheritInverseConfiguration
    EmployeeDto toDto(Employee employee);

    @Mapping(source = "firstName", target = "user.person.firstName")
    @Mapping(source = "lastName", target = "user.person.lastName")
    Employee toEntity(EmployeeNameDto employeeNameDto);
    
    default EmployeeNameDto convertEmployeeToEmployeeNameDto(Employee employee) {
       EmployeeNameDto employeeNameDto = new EmployeeNameDto();

       employeeNameDto.setFirstName(employee.getUser().getPerson().getFirstName());
       employeeNameDto.setLastName(employee.getUser().getPerson().getLastName());

       return employeeNameDto;
    }


}

