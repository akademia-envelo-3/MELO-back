package pl.envelo.melo.domain.poll;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class PollTemplate {
    @Id
    @GeneratedValue
    private int id;
    private String pollQuestion;
    @OneToMany
    private Set<PollQuestion> pollOptions;
    private boolean multiChoice;
}
