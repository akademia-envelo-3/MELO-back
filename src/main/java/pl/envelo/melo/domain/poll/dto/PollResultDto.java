package pl.envelo.melo.domain.poll.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@NoArgsConstructor
@Setter
@Getter
public class PollResultDto {
    private String pollQuestion;
    private Set<PollAnswerResultDto> pollAnswers;
}
