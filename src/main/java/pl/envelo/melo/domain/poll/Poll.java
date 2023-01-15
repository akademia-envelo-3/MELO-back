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
public class Poll {
    @Id
    @GeneratedValue
    private int id;
    @OneToOne
    private PollTemplate pollTemplate;
    @OneToMany
    private Set<PollAnswer> pollAnswers;

}


