package pl.envelo.melo.domain.poll;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.envelo.melo.authorization.employee.Employee;

import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class PollAnswer {
    @Id
    @GeneratedValue
    private int id;
    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;
    private Set<String> pollResult;
}
