package pl.envelo.melo.domain.poll.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.envelo.melo.domain.poll.PollConst;


@NoArgsConstructor
@Setter
@Getter
public class PollQuestionDto {

    @Size(min = PollConst.MIN_QUESTION_CHARACTER_LIMIT, max = PollConst.MAX_QUESTION_CHARACTER_LIMIT)
    private String pollQuestion;
    @NotNull
    private Integer pollId;
}
