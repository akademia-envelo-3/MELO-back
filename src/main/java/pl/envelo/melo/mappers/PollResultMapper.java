package pl.envelo.melo.mappers;

import org.mapstruct.Mapper;
import pl.envelo.melo.domain.poll.Poll;
import pl.envelo.melo.domain.poll.dto.PollResultDto;

@Mapper(componentModel = "spring", uses = PollAnswerResultMapper.class)
public interface PollResultMapper extends EntityMapper<PollResultDto, Poll> {


}
