package pl.envelo.melo.authorization.mailtoken;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import pl.envelo.melo.authorization.person.Person;
import pl.envelo.melo.domain.event.Event;

@Entity
@Getter
@Setter
@AllArgsConstructor
public class MailToken {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    int id;
    String token;
    Person person;
    Event event;
}
