package pl.envelo.melo.mappers;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import pl.envelo.melo.domain.poll.PollAnswer;
import pl.envelo.melo.domain.poll.dto.PollAnswerDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PollAnswerMapper extends EntityMapper<PollAnswerDto, PollAnswer> {


    List<PollAnswer> toEntity(List<PollAnswerDto> pollAnswer);

    @InheritInverseConfiguration
    List<PollAnswerDto> toDto(List<PollAnswer> pollAnswer);
}
