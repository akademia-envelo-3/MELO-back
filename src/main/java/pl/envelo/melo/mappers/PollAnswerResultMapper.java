package pl.envelo.melo.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.envelo.melo.authorization.employee.Employee;
import pl.envelo.melo.domain.poll.PollAnswer;
import pl.envelo.melo.domain.poll.dto.PollAnswerResultDto;

import java.util.Set;

@Mapper(componentModel = "spring")
public interface PollAnswerResultMapper extends EntityMapper<PollAnswerResultDto, PollAnswer> {

    default int map(Set<Employee> set) {
        return set.size();
    }

    @Mapping(source = "employee", target = "result")
    PollAnswerResultDto toDto(PollAnswer pollAnswer);
}
