package pl.envelo.melo.mappers;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import pl.envelo.melo.domain.poll.Poll;
import pl.envelo.melo.domain.poll.dto.PollTemplateToDisplayOnListDto;

@Mapper(componentModel = "spring")
public interface PollToDisplayOnListDtoMapper {

    @InheritInverseConfiguration
    PollTemplateToDisplayOnListDto convert(Poll poll);

}
