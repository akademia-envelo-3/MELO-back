package pl.envelo.melo.authorization.employee;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.envelo.melo.authorization.employee.dto.EmployeeDto;
import pl.envelo.melo.authorization.person.Person;
import pl.envelo.melo.authorization.person.PersonRepository;
import pl.envelo.melo.domain.event.dto.EventToDisplayOnListDto;

import java.util.List;
import java.util.Optional;
import java.util.Set;


@Service
public class EmployeeService {

    private EmployeeRepository employeeRepository;
    private PersonRepository personRepository;

    public EmployeeService(EmployeeRepository employeeRepository, PersonRepository personRepository){
        this.employeeRepository = employeeRepository;
        this.personRepository = personRepository;
    }

    public ResponseEntity<EmployeeDto> getEmployee(int id){
//        return employeeRepository.findById(id);
        return null;
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
