package pl.envelo.melo.mappers;

import org.mapstruct.*;
import pl.envelo.melo.authorization.employee.dto.EmployeeNameDto;
import pl.envelo.melo.authorization.person.Person;
import pl.envelo.melo.domain.event.Event;
import pl.envelo.melo.domain.event.dto.EventDetailsDto;
import pl.envelo.melo.domain.poll.Poll;
import pl.envelo.melo.domain.poll.PollTemplate;
import pl.envelo.melo.domain.poll.dto.PollTemplateDto;
import pl.envelo.melo.domain.poll.dto.PollTemplateToDisplayOnListDto;


import java.util.*;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {HashtagMapper.class, AttachmentMapper.class, EmployeeMapper.class,
                                            LocationMapper.class, CategoryMapper.class, PollTemplateMapper.class})
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
       List<PollTemplateToDisplayOnListDto> pollQuestionList = new ArrayList<>();
       Set<Poll> pollSet = event.getPolls();
        for (Poll poll : pollSet) {

            PollTemplateDto pollTemplateDto = new PollTemplateDto();
            PollTemplateToDisplayOnListDto pollQuestion = new PollTemplateToDisplayOnListDto();

            pollTemplateDto.setPollQuestion(poll.getPollTemplate().getPollQuestion());
            pollQuestion.setPollQuestion(poll.getPollTemplate().getPollQuestion());

            pollTemplateDto.setPollOption(new ArrayList<>(poll.getPollTemplate().getPollOptions()));
            pollQuestion.setPollOption(new ArrayList<>(poll.getPollTemplate().getPollOptions()));

            pollTemplateDto.setMultiChoice(poll.getPollTemplate().isMultiChoice());
            pollQuestion.setMultiChoice(poll.getPollTemplate().isMultiChoice());

            pollTemplateDto.setEventId(event.getId());
            pollQuestion.setPollId(poll.getId());

            pollTemplateDtoList.add(pollTemplateDto);
            pollQuestionList.add(pollQuestion);
        }

        eventDetailsDto.setPolls(pollTemplateDtoList);
        eventDetailsDto.setPollQuestion(pollQuestionList);

    }
}
