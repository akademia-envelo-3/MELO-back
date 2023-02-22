package pl.envelo.melo.domain.poll.dto;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.envelo.melo.domain.poll.PollConst;

@NoArgsConstructor
@Setter
@Getter
public class NewPollAnswerDto {

    @Size(min = PollConst.MIN_QUESTION_CHARACTER_LIMIT, max = PollConst.MAX_QUESTION_CHARACTER_LIMIT)
    private String pollAnswer;

}