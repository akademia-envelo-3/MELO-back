package pl.envelo.melo.domain.poll;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    @OneToOne
    @NotNull
    @PrimaryKeyJoinColumn(name = "poll_template_id")
    private PollTemplate pollTemplate;
    @OneToMany
    private Set<PollAnswer> pollAnswers;

}


