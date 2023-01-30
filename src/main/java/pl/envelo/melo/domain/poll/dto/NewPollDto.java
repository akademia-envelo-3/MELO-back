package pl.envelo.melo.domain.poll.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NewPollDto {

    private String pollQuestion;
    private Set<NewPollAnswerDto> pollAnswers;
    private boolean multichoice;

}