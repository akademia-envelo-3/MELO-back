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
public class PollTemplateDto {
    @Max(1000)
    private String pollQuestion;
    @Min(2)
    @Max(10)
    @NotNull
    private Set<String> pollOptions;
    @NotNull
    private Boolean multiChoice;
    @NotNull
    private Integer eventId;
}
