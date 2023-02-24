package pl.envelo.melo.mappers;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import pl.envelo.melo.domain.poll.PollAnswer;
import pl.envelo.melo.domain.poll.dto.PollAnswerDto;

import java.util.Set;

@Mapper(componentModel = "spring")
public interface PollAnswerMapper extends EntityMapper<PollAnswerDto, PollAnswer> {

    Set<PollAnswer> toEntity(Set<PollAnswerDto> pollAnswer);

    @InheritInverseConfiguration
    Set<PollAnswerDto> toDto(Set<PollAnswer> pollAnswer);
}
