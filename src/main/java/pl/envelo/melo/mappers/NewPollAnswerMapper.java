package pl.envelo.melo.mappers;

import org.mapstruct.Mapper;
import pl.envelo.melo.domain.poll.dto.NewPollAnswerDto;
import pl.envelo.melo.domain.poll.dto.PollAnswerDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface NewPollAnswerMapper extends EntityMapper<PollAnswerDto, NewPollAnswerDto> {

    List<PollAnswerDto> toPollAnswerDto(List<NewPollAnswerDto> newPollAnswerDtoSet);

}
