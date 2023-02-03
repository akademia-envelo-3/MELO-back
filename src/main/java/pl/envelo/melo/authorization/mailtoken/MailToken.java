package pl.envelo.melo.authorization.mailtoken;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.envelo.melo.authorization.person.Person;
import pl.envelo.melo.domain.event.Event;

import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MailToken {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    UUID token;
    @OneToOne
    Person person;
    @ManyToOne
    Event event;
}
