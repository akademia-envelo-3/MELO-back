package pl.envelo.melo.domain.poll;

import pl.envelo.melo.domain.poll.dto.PollTemplateDto;

import java.util.Set;

public class PollDto {
    PollTemplateDto pollTemplateDto;
    Set<PollAnswer> pollAnswers;
}
