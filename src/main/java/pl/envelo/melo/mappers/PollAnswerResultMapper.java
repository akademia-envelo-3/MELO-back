package pl.envelo.melo.mappers;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import pl.envelo.melo.authorization.employee.Employee;
import pl.envelo.melo.domain.event.Event;
import pl.envelo.melo.domain.event.dto.EventToDisplayOnListDto;
import pl.envelo.melo.domain.poll.PollAnswer;
import pl.envelo.melo.domain.poll.dto.PollAnswerDto;
import pl.envelo.melo.domain.poll.dto.PollAnswerResultDto;
import pl.envelo.melo.domain.poll.dto.PollResultDto;

import java.util.Set;

@Mapper(componentModel = "spring")
public interface PollAnswerResultMapper extends EntityMapper<PollAnswerResultDto, PollAnswer> {


    PollAnswerResultDto toDto(PollAnswer pollAnswer);
    @AfterMapping
    default void updateResult(PollAnswer pollAnswer, @MappingTarget PollAnswerResultDto pollAnswerResultDto){
        pollAnswerResultDto.setResult(pollAnswer.getEmployee().size());
    }
}
