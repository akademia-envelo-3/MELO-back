package pl.envelo.melo.domain.poll.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.envelo.melo.domain.poll.PollAnswer;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PollDto {

//    PollTemplateDto pollTemplateDto;
//    Set<PollAnswer> pollAnswers;
    private String pollQuestion;
    private Set<PollAnswerDto> pollAnswers;
    private boolean multichoice;

}
