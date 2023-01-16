package pl.envelo.melo.domain.poll.dto;

import jakarta.validation.constraints.Max;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@Setter
@Getter
public class PollQuestionDto {

    @Max(1000)
    private String pollQuestion;
    private int pollId;
}
