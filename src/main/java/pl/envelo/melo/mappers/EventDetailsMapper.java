package pl.envelo.melo.mappers;

import org.mapstruct.*;
import pl.envelo.melo.authorization.employee.dto.EmployeeNameDto;
import pl.envelo.melo.authorization.person.Person;
import pl.envelo.melo.domain.event.Event;
import pl.envelo.melo.domain.event.dto.EventDetailsDto;
import pl.envelo.melo.domain.poll.Poll;
import pl.envelo.melo.domain.poll.dto.PollQuestionDto;
import pl.envelo.melo.domain.poll.dto.PollTemplateDto;

import java.util.*;

@Mapper(componentModel = "spring", uses = {HashtagMapper.class, AttachmentMapper.class, EmployeeMapper.class,
                                            LocationMapper.class, CategoryMapper.class})
public interface EventDetailsMapper {

   EventDetailsDto convert(Event event);

    @AfterMapping
    default void update(Event event, @MappingTarget EventDetailsDto eventDetailsDto){

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

       List<PollTemplateDto> pollTemplateDtoList = new ArrayList<>();
       List<PollQuestionDto> pollQuestionList = new ArrayList<>();
       Set<Poll> pollSet = event.getPolls();
        for (Poll poll : pollSet) {

            PollTemplateDto pollTemplateDto = new PollTemplateDto();
            PollQuestionDto pollQuestion = new PollQuestionDto();

            pollTemplateDto.setPollQuestion(poll.getPollTemplate().getPollQuestion());
            pollQuestion.setPollQuestion(poll.getPollTemplate().getPollQuestion());

            pollTemplateDto.setPollOptions(new HashSet<>(poll.getPollTemplate().getPollOptions()));
            pollTemplateDto.setMultiChoice(poll.getPollTemplate().isMultiChoice());

            pollQuestion.setPollId(poll.getId());

            pollTemplateDtoList.add(pollTemplateDto);
            pollQuestionList.add(pollQuestion);
        }

        eventDetailsDto.setPolls(pollTemplateDtoList);
        eventDetailsDto.setPollQuestion(pollQuestionList);

    }
}
