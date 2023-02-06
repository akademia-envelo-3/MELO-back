package pl.envelo.melo.authorization.user;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.envelo.melo.authorization.person.Person;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByPerson(Person person);
}
