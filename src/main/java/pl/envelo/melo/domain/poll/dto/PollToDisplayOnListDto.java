package pl.envelo.melo.domain.poll.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.envelo.melo.domain.poll.PollConst;

import java.util.Set;

@NoArgsConstructor
@Setter
@Getter
public class PollToDisplayOnListDto {

    @Size(min = PollConst.MIN_QUESTION_CHARACTER_LIMIT, max = PollConst.MAX_QUESTION_CHARACTER_LIMIT
            , message = "Poll answer must not contain less than " + PollConst.MIN_QUESTION_CHARACTER_LIMIT +
            " and more than " + PollConst.MAX_QUESTION_CHARACTER_LIMIT)
    private String pollQuestion;
    @NotNull
    private int pollId;
    private boolean filled;

}
