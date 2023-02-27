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
    @Size(min = PollConst.MIN_QUESTION_CHARACTER_LIMIT, max = PollConst.MAX_QUESTION_CHARACTER_LIMIT
            , message = "Poll answer must not contain less than " + PollConst.MIN_QUESTION_CHARACTER_LIMIT +
            " and more than " + PollConst.MAX_QUESTION_CHARACTER_LIMIT)
    private String pollQuestion;
    @Size(min = PollConst.MIN_OPTION_COUNT, max = PollConst.MAX_OPTION_COUNT
            , message = "Poll must not have less than " + PollConst.MIN_OPTION_COUNT +
            " and more than " + PollConst.MAX_OPTION_COUNT)
    private List<PollAnswerResultDto> pollAnswers;

}
