package pl.envelo.melo.domain.poll.dto;

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
public class PollAnswerResultDto {

    @NotBlank
    @Size(max=PollConst.OPTION_CHARACTER_LIMIT)
    private String pollAnswer;
    @NotNull
    private int result;
}
