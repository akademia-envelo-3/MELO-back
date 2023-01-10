package pl.envelo.melo.domain.poll.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@NoArgsConstructor
@Setter
@Getter
public class PollAnswerDto {
    private int employeeId;
    @Min(2)
    @Max(10)
    private Set<String> pollResult;
    private int pollId;
}
