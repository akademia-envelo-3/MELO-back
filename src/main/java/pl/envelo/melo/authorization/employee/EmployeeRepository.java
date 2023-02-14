package pl.envelo.melo.authorization.employee;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.envelo.melo.authorization.person.Person;
import pl.envelo.melo.authorization.user.User;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
    Optional<Employee> findByUser(User user);
    Optional<Employee> findByUserId(UUID uuid);
    Optional<Employee> findByUserPersonEmail(String email);
    Optional<Employee> findByUserPerson(Person person);
}
