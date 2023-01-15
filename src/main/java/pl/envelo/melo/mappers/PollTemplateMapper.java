package pl.envelo.melo.mappers;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import pl.envelo.melo.authorization.employee.Employee;
import pl.envelo.melo.authorization.employee.dto.EmployeeNameDto;
import pl.envelo.melo.domain.event.Event;
import pl.envelo.melo.domain.poll.PollTemplate;
import pl.envelo.melo.domain.poll.dto.PollTemplateDto;

import java.util.*;

@Mapper(componentModel = "spring")
public interface PollTemplateMapper {

//    PollTemplate convert(PollTemplateDto pollTemplateDto);
    @InheritInverseConfiguration
    PollTemplateDto convert(PollTemplate pollTemplate);
//    default PollTemplateDto convert(PollTemplate pollTemplate, Event event){
//        PollTemplateDto pollTemplateDto = new PollTemplateDto();
//        pollTemplateDto.setPollQuestion(pollTemplate.getPollQuestion());
//        pollTemplateDto.setPollOption(new ArrayList<>(pollTemplate.getPollOptions()));
//        pollTemplateDto.setMultiChoice(pollTemplate.isMultiChoice());
//        pollTemplateDto.setEventId(event.getId());
//        return  pollTemplateDto;
//    }
}
