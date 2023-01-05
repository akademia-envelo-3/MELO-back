package pl.envelo.melo.authorization.employee;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.envelo.melo.authorization.employee.dto.EmployeeDto;
import pl.envelo.melo.domain.event.Event;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
public class EmployeeController {

    private final EmployeeService employeeService;


    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    public List<EmployeeDto> getEmployees(){
        return employeeService.getEmployees();
    }

    @GetMapping
    public Optional<EmployeeDto> getEmployee(int id){
        return employeeService.getEmployee(id);
    }

    @GetMapping
    public Set<EventDto> getOwnedEvents(){
        return employeeService.getSetOfOwnedEvents();
    }
}
