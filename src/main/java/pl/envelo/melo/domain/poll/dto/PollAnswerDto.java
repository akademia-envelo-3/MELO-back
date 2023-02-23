package pl.envelo.melo.domain.poll.dto;

import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.envelo.melo.domain.poll.PollConst;

import java.util.Set;

@NoArgsConstructor
@Setter
@Getter
public class PollAnswerDto {

    @NotNull
    private int id;
    @NotBlank
    @Size(max=PollConst.OPTION_CHARACTER_LIMIT, message = "Poll answer must not have more than "+PollConst.OPTION_CHARACTER_LIMIT)
    private String pollAnswer;

}
