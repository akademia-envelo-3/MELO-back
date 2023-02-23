package pl.envelo.melo.domain.poll.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@NoArgsConstructor
@Setter
@Getter
public class PollSendResultDto {

    @NotNull(message = "Poll id must not be null")
    private int pollId;

    private Set<Integer> pollAnswerId;
}
