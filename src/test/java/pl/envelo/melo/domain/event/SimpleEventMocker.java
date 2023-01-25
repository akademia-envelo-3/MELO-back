package pl.envelo.melo.domain.event;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import pl.envelo.melo.authorization.employee.Employee;
import pl.envelo.melo.authorization.employee.EmployeeRepository;
import pl.envelo.melo.authorization.person.Person;
import pl.envelo.melo.authorization.person.PersonRepository;
import pl.envelo.melo.authorization.user.User;
import pl.envelo.melo.authorization.user.UserRepository;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
public class SimpleEventMocker {
    private EmployeeRepository employeeRepository;
    private EventRepository eventRepository;
    private PersonRepository personRepository;
    private UserRepository userRepository;
    static int id = 1000;

    public Event mockEvent(LocalDateTime localDateTime, EventType eventType, Employee... employees) {
        Event event = new Event();
        event.setId(id++);
        event.setDescription("testdesc");
        event.setName("test");
        event.setStartTime(localDateTime);
        event.setEndTime(localDateTime.plusDays(5));
        event.setType(eventType);
        event.setInvited(new HashSet<>());
//        event.setMembers(new HashSet<>());
        event.setAttachments(new HashSet<>());
        event.setTheme(Theme.BLUE);

        if (employees.length != 0) {
            event.setOrganizer(employees[0]);
            Set<Person> employees1 = new HashSet<>();
            employees1.add(employees[0].getUser().getPerson());
            event.setMembers(employees1);
        }else {
            Employee owner = mockEmployee("owner");
            event.setOrganizer(owner);
            Set<Person> employees1 = new HashSet<>();
            employees1.add(owner.getUser().getPerson());
            event.setMembers(employees1);
        }
        eventRepository.save(event);
        Arrays.stream(employees).map(e -> {
            return e.getUser().getPerson();
        }).sequential().forEach(event.getMembers()::add);

        return eventRepository.save(event);
    }

    public Employee mockEmployee(String name) {
        Person person = new Person();
        person.setId(id);
        person.setFirstName(name);
        person.setLastName(name);
        person.setEmail(String.format("%s@%s.pl", name, name));

        User user = new User();
        user.setId(id);
        user.setPerson(personRepository.save(person));
        user.setPassword(name);

        Employee employee = new Employee();
        employee.setId(id);
        employee.setUser(userRepository.save(user));

        return employeeRepository.save(employee);
    }
}
