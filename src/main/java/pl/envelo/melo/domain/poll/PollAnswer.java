package pl.envelo.melo.domain.poll;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import pl.envelo.melo.authorization.employee.Employee;

import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "poll_answers")
public class PollAnswer {
    @Id
    @GeneratedValue
    private int id;
    @NotBlank
    @Size(max=PollConst.OPTION_CHARACTER_LIMIT)
    private String pollAnswer;
    @ManyToOne
    @JoinColumn(name="poll_id")
    private Poll poll;
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Employee> employee;
}
