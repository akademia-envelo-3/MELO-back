package pl.envelo.melo.domain.poll.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@Setter
@Getter
public class PollQuestionDto {
    @NotBlank
    @Max(1000)
    private String pollQuestion;
    @NotNull
    private Integer pollId;
}
