package pl.envelo.melo.domain.poll;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "poll_templates")
public class PollTemplate {
    @Id
    @GeneratedValue
    private int id;
    @Column(nullable = false)
    private String pollQuestion;
    @Column(nullable = false)
    @Size(min = 2, max = 10)
    private Set<String> pollOptions;
    @Column(nullable = false)
    private boolean multiChoice;

}
