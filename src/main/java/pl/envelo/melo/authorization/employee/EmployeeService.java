package pl.envelo.melo.authorization.employee;

import org.springframework.stereotype.Service;
import pl.envelo.melo.authorization.employee.dto.EmployeeDto;
import pl.envelo.melo.authorization.person.Person;
import pl.envelo.melo.authorization.person.PersonRepository;

import java.util.List;
import java.util.Optional;


@Service
public class EmployeeService {

    private EmployeeRepository employeeRepository;
    private PersonRepository personRepository;

    public EmployeeService(EmployeeRepository employeeRepository, PersonRepository personRepository){
        this.employeeRepository = employeeRepository;
        this.personRepository = personRepository;
    }

    public Optional<EmployeeDto> getEmployee(int id){
//        return employeeRepository.findById(id);
        return null;
    }

    public Optional<Person> getPerson(int employeeId){
//        return personRepository.findById(employeeId);
        return null;
    }

    public List<EmployeeDto> getEmployees(){
//        return employeeRepository.findAll();
        return null;
    }

    public Set<EventDto> getSetOfOwnedEvents(){
        return null;
    }
}
