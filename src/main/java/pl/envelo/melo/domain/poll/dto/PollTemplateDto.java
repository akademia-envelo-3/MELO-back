package pl.envelo.melo.domain.poll.dto;

import jakarta.validation.constraints.Max;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
    @NotNull
    private Boolean multiChoice;
    @Size(min = 2, max = 10)
    private Set<String> pollOption;
}
