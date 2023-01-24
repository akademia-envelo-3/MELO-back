package pl.envelo.melo.domain.poll.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@NoArgsConstructor
@Setter
@Getter
public class PollTemplateDto {
    @Size(max = 1000)
    @NotBlank
    private String pollQuestion;
    @NotNull
    private Boolean multiChoice;
    private Set<String> pollOptions;
}
