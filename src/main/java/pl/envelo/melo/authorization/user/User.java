package pl.envelo.melo.authorization.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    @NotBlank
    private String password;
    @NotBlank
    @OneToOne
    private Person person;

}
