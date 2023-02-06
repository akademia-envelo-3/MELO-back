package pl.envelo.melo.authorization.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import pl.envelo.melo.authorization.person.Person;

import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    private UUID id;
    @NotNull
    @OneToOne(optional = false)
    private Person person;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User user)) return false;
        return getPerson().equals(user.getPerson());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPerson());
    }
}
