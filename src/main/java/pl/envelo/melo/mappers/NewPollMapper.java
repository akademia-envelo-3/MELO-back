package pl.envelo.melo.mappers;

import org.mapstruct.Mapper;
import pl.envelo.melo.domain.poll.dto.NewPollDto;
import pl.envelo.melo.domain.poll.dto.PollDto;

@Mapper(componentModel = "spring", uses = NewPollAnswerMapper.class)
public interface NewPollMapper extends EntityMapper<PollDto, NewPollDto> {
}
