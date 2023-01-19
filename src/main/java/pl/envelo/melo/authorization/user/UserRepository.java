package pl.envelo.melo.authorization.user;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.envelo.melo.authorization.person.Person;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByPerson(Person person);
}
