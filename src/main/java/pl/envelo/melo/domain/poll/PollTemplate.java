package pl.envelo.melo.domain.poll;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
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
    private Set<String> pollOptions;
    private boolean multiChoice;

}
