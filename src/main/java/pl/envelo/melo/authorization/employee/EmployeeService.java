package pl.envelo.melo.authorization.employee;


import org.mapstruct.Mapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.Mapping;
import pl.envelo.melo.authorization.employee.dto.EmployeeDto;
import pl.envelo.melo.authorization.person.Person;
import pl.envelo.melo.domain.event.dto.EventToDisplayOnListDto;
import pl.envelo.melo.mappers.EmployeeMapper;
import pl.envelo.melo.authorization.person.PersonRepository;
import pl.envelo.melo.domain.event.Event;
import pl.envelo.melo.domain.event.dto.EventToDisplayOnListDto;
import pl.envelo.melo.mappers.EventMapper;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Service
public class EmployeeService {


    private EmployeeRepository employeeRepository;
    private final EventMapper eventMapper;
    private PersonRepository personRepository;

    public EmployeeService(EmployeeRepository employeeRepository, EventMapper eventMapper, PersonRepository personRepository){
        this.employeeRepository = employeeRepository;
        this.eventMapper = eventMapper;
        this.personRepository = personRepository;
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


    public ResponseEntity<?> getSetOfOwnedEvents(int id){
        if(employeeRepository.existsById(id)) {
            Set<Event> events = employeeRepository.findById(id).get().getOwnedEvents();
            return ResponseEntity.ok(events.stream().map(eventMapper::convert).collect(Collectors.toSet()));
        } else{
            return ResponseEntity.status(404).body("Employee with this ID do not exist");
        }

    }
}
