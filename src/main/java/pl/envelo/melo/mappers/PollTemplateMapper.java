package pl.envelo.melo.mappers;

import org.mapstruct.Mapper;
import pl.envelo.melo.domain.poll.PollTemplate;
import pl.envelo.melo.domain.poll.dto.PollTemplateDto;

@Mapper
public interface PollTemplateMapper {
    PollTemplateDto convert(PollTemplate pollTemplate);
}
