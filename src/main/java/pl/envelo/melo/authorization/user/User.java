package pl.envelo.melo.authorization.user;

import jakarta.persistence.*;
import lombok.*;
import pl.envelo.melo.authorization.person.Person;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String password;
    @OneToOne
    private Person person;

}
