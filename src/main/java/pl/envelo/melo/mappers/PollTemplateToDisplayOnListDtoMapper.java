package pl.envelo.melo.mappers;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import pl.envelo.melo.domain.event.Event;
import pl.envelo.melo.domain.poll.Poll;
import pl.envelo.melo.domain.poll.PollAnswer;
import pl.envelo.melo.domain.poll.PollTemplate;
import pl.envelo.melo.domain.poll.dto.PollTemplateDto;
import pl.envelo.melo.domain.poll.dto.PollTemplateToDisplayOnListDto;

import java.util.ArrayList;

@Mapper(componentModel = "spring")
public interface PollTemplateToDisplayOnListDtoMapper {

//    PollTemplate convert(PollTemplateToDisplayOnListDto pollTemplateToDisplayOnListDto);
    @InheritInverseConfiguration
    PollTemplateToDisplayOnListDto convert(PollTemplate pollTemplate);

//    default PollTemplateToDisplayOnListDto convert(PollTemplate pollTemplate, Poll poll){
//        PollTemplateToDisplayOnListDto pollTemplateToDisplayOnListDto = new PollTemplateToDisplayOnListDto();
//        pollTemplateDto.setPollQuestion(pollTemplate.getPollQuestion());
//        pollTemplateDto.setPollOption(new ArrayList<>(pollTemplate.getPollOptions()));
//        pollTemplateDto.setMultiChoice(pollTemplate.isMultiChoice());
//        pollTemplateDto.setPollId(poll.getId());
//        return  pollTemplateDto;
//    }
}
