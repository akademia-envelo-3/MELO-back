package pl.envelo.melo.domain.poll;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.envelo.melo.domain.poll.dto.PollAnswerDto;

import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "polls")
public class Poll {
    @Id
    @GeneratedValue
    private int id;
    @Column(nullable = false)
    private String pollQuestion;
    @Column
    private boolean multichoice;
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "poll")
    private Set<PollAnswer> pollAnswers;

}


