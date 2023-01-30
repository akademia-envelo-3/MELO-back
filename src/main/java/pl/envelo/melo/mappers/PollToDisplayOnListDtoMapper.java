package pl.envelo.melo.mappers;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.envelo.melo.domain.poll.Poll;
import pl.envelo.melo.domain.poll.dto.PollToDisplayOnListDto;

import java.util.Set;

@Mapper(componentModel = "spring")
public interface PollToDisplayOnListDtoMapper {

    Poll convert(PollToDisplayOnListDto poll);
    @InheritInverseConfiguration
    @Mapping(target = "pollId", source = "id")
    PollToDisplayOnListDto convert(Poll poll);

//    Set<Poll> convert(Set<PollToDisplayOnListDto> poll);
//    @InheritInverseConfiguration
    Set<PollToDisplayOnListDto> convert(Set<Poll> poll);

}
