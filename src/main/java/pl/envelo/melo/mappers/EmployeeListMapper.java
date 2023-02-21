package pl.envelo.melo.mappers;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.envelo.melo.authorization.employee.Employee;
import pl.envelo.melo.authorization.employee.EmployeeService;
import pl.envelo.melo.authorization.employee.dto.EmployeeListDto;

@Mapper(componentModel = "spring", uses = { EmployeeService.class })
public interface EmployeeListMapper extends EntityMapper<EmployeeListDto, Employee> {
    @Mapping(source = "firstName", target = "user.person.firstName")
    @Mapping(source = "lastName", target = "user.person.lastName")
    Employee toEntity(EmployeeListDto employeeDto);

    @InheritInverseConfiguration
    EmployeeListDto toDto(Employee employee);
}
