package pl.envelo.melo.domain.poll.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@NoArgsConstructor
@Setter
@Getter
public class PollTemplateToDisplayOnListDto {
    @Max(1000)
    private String pollQuestion;
    @Size(min =2, max = 10)
    private Set<String> pollOption;
    private boolean multiChoice;
    private int pollId;
}
