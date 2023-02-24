package pl.envelo.melo;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.envelo.melo.authorization.employee.EmployeeRepository;
import pl.envelo.melo.authorization.person.PersonRepository;
import pl.envelo.melo.authorization.user.UserRepository;
import pl.envelo.melo.domain.event.EventRepository;
import pl.envelo.melo.domain.event.SimpleEventGenerator;
import pl.envelo.melo.utils.H2Utils;

import javax.sql.DataSource;

@Transactional
@SpringBootTest
public abstract class EventContextTest {
    @Autowired
    protected DataSource dataSource;
    @Autowired
    protected EmployeeRepository employeeRepository;
    @Autowired
    protected EventRepository eventRepository;
    protected SimpleEventGenerator simpleEventGenerator;
    @Autowired
    protected PersonRepository personRepository;
    @Autowired
    protected UserRepository userRepository;

    @BeforeEach
    void setUp() {
        H2Utils.clearDb(dataSource);
        if (simpleEventGenerator == null)
            simpleEventGenerator = new SimpleEventGenerator(employeeRepository, eventRepository, personRepository, userRepository);
    }
}
