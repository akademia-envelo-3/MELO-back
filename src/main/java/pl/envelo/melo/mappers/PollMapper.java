package pl.envelo.melo.mappers;

import org.mapstruct.Mapper;
import pl.envelo.melo.domain.poll.Poll;
import pl.envelo.melo.domain.poll.dto.PollDto;

@Mapper(componentModel = "spring")
public interface PollMapper extends EntityMapper<PollDto, Poll> {
}
