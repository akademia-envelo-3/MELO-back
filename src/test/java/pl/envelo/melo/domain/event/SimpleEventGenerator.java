package pl.envelo.melo.domain.event;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import pl.envelo.melo.authorization.AppUser;
import pl.envelo.melo.authorization.admin.Admin;
import pl.envelo.melo.authorization.employee.Employee;
import pl.envelo.melo.authorization.employee.EmployeeRepository;
import pl.envelo.melo.authorization.person.Person;
import pl.envelo.melo.authorization.person.PersonRepository;
import pl.envelo.melo.authorization.user.User;
import pl.envelo.melo.authorization.user.UserRepository;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.*;

@RequiredArgsConstructor
public class SimpleEventGenerator {
    private final EmployeeRepository employeeRepository;
    private final EventRepository eventRepository;
    private final PersonRepository personRepository;
    private final UserRepository userRepository;
    @Value("${melo.admin-role}")
    private String adminRole;
    @Value("${melo.employee-role}")
    private String employeeRole;
    public Event mockEvent(LocalDateTime localDateTime, EventType eventType, Employee... employees) {
        Event event = new Event();
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
        } else {
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
        event.setTheme(Theme.GREEN);
        return eventRepository.save(event);
    }

    public Employee mockEmployee(String name) {
        Person person = new Person();
        person.setFirstName(name);
        person.setLastName(name);
        person.setEmail(String.format("%s@%s.pl", name, name));

        User user = new User();
        user.setPerson(personRepository.save(person));
        user.setId(UUID.randomUUID());
        Employee employee = new Employee();
        employee.setUser(userRepository.save(user));

        return employeeRepository.save(employee);
    }
    public Principal getToken(AppUser appUser){
        JwtAuthenticationToken token = Mockito.mock(JwtAuthenticationToken.class);
        if(appUser instanceof Employee employee){
            Mockito.when(token.getTokenAttributes()).thenReturn(
                    Map.of("sub", employee.getUser().getId().toString(),
                            "email", employee.getUser().getPerson().getEmail(),
                            "given_name",employee.getFirstName(),
                            "family_name",employee.getLastName(),
                            "roles", List.of("Employee")
                    )
            );
        }
        if(appUser instanceof Admin admin){
            Mockito.when(token.getTokenAttributes()).thenReturn(
                    Map.of("sub", admin.getUser().getId().toString(),
                            "email", admin.getUser().getPerson().getEmail(),
                            "given_name",admin.getUser().getPerson().getFirstName(),
                            "family_name",admin.getUser().getPerson().getLastName(),
                            "roles", List.of("Admin")
                    )
            );
        }
        return token;
    }
}
