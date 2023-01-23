package pl.envelo.melo.mappers;

import org.mapstruct.*;
import pl.envelo.melo.domain.poll.Poll;
import pl.envelo.melo.domain.poll.PollTemplate;
import pl.envelo.melo.domain.poll.dto.PollTemplateDto;

@Mapper(componentModel = "spring")
public interface PollTemplateMapper {
    PollTemplateDto convert(PollTemplate pollTemplate);

    @InheritInverseConfiguration
    PollTemplate convert(PollTemplateDto pollTemplateDto);
}
