package pl.envelo.melo.domain.poll.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@NoArgsConstructor
@Setter
@Getter
public class PollToDisplayOnListDto {
    @Max(1000)
    private String pollQuestion;
    private int pollId;
    private boolean filled;

}
