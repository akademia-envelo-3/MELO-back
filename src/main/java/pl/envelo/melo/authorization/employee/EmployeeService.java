package pl.envelo.melo.authorization.employee;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.envelo.melo.authorization.employee.dto.EmployeeDto;
import pl.envelo.melo.authorization.person.Person;
import pl.envelo.melo.domain.event.dto.EventToDisplayOnListDto;
import pl.envelo.melo.mappers.EmployeeMapper;

import java.util.List;
import java.util.Optional;
import java.util.Set;


@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;

    public EmployeeService(EmployeeRepository employeeRepository, EmployeeMapper employeeMapper){
        this.employeeRepository = employeeRepository;
        this.employeeMapper = employeeMapper;
    }

    public ResponseEntity<EmployeeDto> getEmployee(int id) {

        Optional<Employee> employeeOptional = employeeRepository.findById(id);
        if (!employeeOptional.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        EmployeeDto employeeDto = employeeMapper.toDto(employeeOptional.get());
        return new ResponseEntity<>(employeeDto, HttpStatus.OK);

    }




    public ResponseEntity<Person> getPerson(int employeeId){
//        return personRepository.findById(employeeId);
        return null;
    }

    public ResponseEntity<List<EmployeeDto>> getEmployees(){
//        return employeeRepository.findAll();
        return null;
    }

    public ResponseEntity<Set<EventToDisplayOnListDto>> getSetOfOwnedEvents(){

        return null;
    }
}
