package pl.envelo.melo.mappers;

import org.mapstruct.*;
import pl.envelo.melo.authorization.employee.dto.EmployeeNameDto;
import pl.envelo.melo.authorization.person.Person;
import pl.envelo.melo.domain.event.Event;
import pl.envelo.melo.domain.event.dto.EventDetailsDto;
import pl.envelo.melo.domain.poll.Poll;
import pl.envelo.melo.domain.poll.dto.PollDto;
import pl.envelo.melo.domain.poll.dto.PollQuestionDto;
import pl.envelo.melo.domain.poll.dto.PollToDisplayOnListDto;

import java.util.*;

@Mapper(componentModel = "spring", uses = {HashtagMapper.class, AttachmentMapper.class, EmployeeMapper.class,
                                            LocationMapper.class, CategoryMapper.class, PollAnswerMapper.class})
public interface EventDetailsMapper {

    EventDetailsDto convert(Event event);

   //todo fixme bruh

    @AfterMapping
    default void update(Event event, @MappingTarget EventDetailsDto eventDetailsDto) {

        eventDetailsDto.setEventType(event.getType());

        List<EmployeeNameDto> confirmedMembers = new ArrayList<>();
        Set<Person> members = event.getMembers();
        for (Person member : members) {
            EmployeeNameDto confirmedMember = new EmployeeNameDto();
            confirmedMember.setFirstName(member.getFirstName());
            confirmedMember.setLastName(member.getLastName());
            confirmedMembers.add(confirmedMember);
        }

        eventDetailsDto.setConfirmedMembers(confirmedMembers);
        
       Set<PollToDisplayOnListDto> pollDtoList = new HashSet<>();
       Set<Poll> pollSet = event.getPolls();
        for (Poll poll : pollSet) {

           PollToDisplayOnListDto pollToDisplayOnListDto = new PollToDisplayOnListDto();
           pollToDisplayOnListDto.setPollId(poll.getId());
           pollToDisplayOnListDto.setPollQuestion(poll.getPollQuestion());

           pollToDisplayOnListDto.setFilled(false); // fixme

            pollDtoList.add(pollToDisplayOnListDto);
        }

        eventDetailsDto.setPolls(pollDtoList);


    }
}
