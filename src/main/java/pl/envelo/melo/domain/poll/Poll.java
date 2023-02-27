package pl.envelo.melo.domain.poll;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

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
    @Size(min = PollConst.MIN_QUESTION_CHARACTER_LIMIT, max = PollConst.MAX_QUESTION_CHARACTER_LIMIT)
    private String pollQuestion;
    @Column
    private boolean multichoice;
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "poll")
    @Size(min = PollConst.MIN_OPTION_COUNT, max = PollConst.MAX_OPTION_COUNT)
    private List<PollAnswer> pollAnswers;

}


