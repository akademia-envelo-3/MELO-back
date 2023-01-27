package pl.envelo.melo.domain.poll.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class PollAnswerResultDto {

    private String pollAnswer;
    private int result;
}
