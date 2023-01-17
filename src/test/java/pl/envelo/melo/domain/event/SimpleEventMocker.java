package pl.envelo.melo.domain.event;

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

@AllArgsConstructor
public class SimpleEventMocker {
    private EmployeeRepository employeeRepository;
    private EventRepository eventRepository;
    private PersonRepository personRepository;
    private UserRepository userRepository;

    public Event mockEvent(LocalDateTime localDateTime, EventType eventType, Employee... employees) {
        Event event = new Event();
        event.setDescription("testdesc");
        event.setName("test");
        event.setStartTime(localDateTime);
        event.setEndTime(localDateTime.plusDays(5));
        event.setType(eventType);
        event.setMembers(new HashSet<>());
        event.setInvited(new HashSet<>());
        event.setAttachments(new HashSet<>());
        if (employees.length != 0)
            event.setOrganizer(employees[0]);
        else {
            Employee owner = mockEmployee("test");
            event.setOrganizer(owner);
            event.getMembers().add(owner.getUser().getPerson());
        }

        Arrays.stream(employees).map(e -> {
            return e.getUser().getPerson();
        }).sequential().forEach(event.getMembers()::add);

        return eventRepository.save(event);
    }

    public Employee mockEmployee(String name) {
        Person person = new Person();
        person.setFirstName(name);
        person.setLastName(name);
        person.setEmail(String.format("%s@%s.pl", name, name));

        User user = new User();
        user.setPerson(personRepository.save(person));
        user.setPassword(name);

        Employee employee = new Employee();
        employee.setUser(userRepository.save(user));

        return employeeRepository.save(employee);
    }
}
