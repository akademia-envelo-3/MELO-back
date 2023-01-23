package pl.envelo.melo.domain.poll.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@NoArgsConstructor
@Setter
@Getter
public class PollAnswerDto {
    @NotNull
    private Integer employeeId;
    @Min(2)
    @Max(10)
    @NotNull
    private Set<String> pollResult;
    @NotNull
    private Integer pollId;
}
