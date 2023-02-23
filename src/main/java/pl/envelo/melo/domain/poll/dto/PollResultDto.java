package pl.envelo.melo.domain.poll.dto;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.envelo.melo.domain.poll.PollConst;

import java.util.List;
import java.util.Set;

@NoArgsConstructor
@Setter
@Getter
public class PollResultDto {

    @Size(min = PollConst.MIN_QUESTION_CHARACTER_LIMIT, max = PollConst.MAX_QUESTION_CHARACTER_LIMIT)
    private String pollQuestion;
    @Size(min = PollConst.MIN_OPTION_COUNT, max = PollConst.MAX_OPTION_COUNT)
    private List<PollAnswerResultDto> pollAnswers;
}
