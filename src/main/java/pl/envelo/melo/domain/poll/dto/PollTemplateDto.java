package pl.envelo.melo.domain.poll.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Setter
@Getter
public class PollTemplateDto {
    @Max(1000)
    private String pollQuestion;
    @Min(2)
    @Max(10)
    private List<String> pollOption;
    private boolean multiChoice;
    private int eventId;
}
