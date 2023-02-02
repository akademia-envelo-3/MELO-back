package pl.envelo.melo.domain.poll;

import lombok.AllArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.envelo.melo.authorization.employee.Employee;
import pl.envelo.melo.domain.event.Event;
import pl.envelo.melo.domain.event.EventRepository;
import pl.envelo.melo.domain.poll.dto.NewPollDto;
import pl.envelo.melo.domain.poll.dto.PollAnswerDto;
import pl.envelo.melo.domain.poll.dto.PollDto;
import pl.envelo.melo.domain.poll.dto.PollToDisplayOnListDto;
import pl.envelo.melo.exceptions.ResourceNotFoundException;
import pl.envelo.melo.mappers.*;

import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PollService {
    private final PollRepository pollRepository;
    private final PollAnswerRepository pollAnswerRepository;
    private final EventRepository eventRepository;
    private final PollMapper pollMapper;
    private final NewPollMapper newPollMapper;
    private final PollToDisplayOnListDtoMapper pollToDisplayOnListDtoMapper;


    public ResponseEntity<List<Integer>> calculatePollResults(int pollId) {
        return null;
    }


    public ResponseEntity<?> insertNewPoll(NewPollDto newPollDto, int eventId) {

        if (eventRepository.findById(eventId).isEmpty()) {
            return ResponseEntity.status(404).body(String.format(PollConst.EVENT_NOT_FOUND, eventId));
        }

        PollDto pollDto = newPollMapper.toDto(newPollDto);

        if (Objects.isNull(pollDto.getPollQuestion())
                || pollDto.getPollQuestion().length() < PollConst.MIN_QUESTION_CHARACTER_LIMIT
                || pollDto.getPollQuestion().length() > PollConst.MAX_QUESTION_CHARACTER_LIMIT) {
            return ResponseEntity.badRequest().body(PollConst.POLL_QUESTION_OUT_OF_RANGE);
        }

        if (Objects.isNull(pollDto.getPollAnswers())
                || pollDto.getPollAnswers().size() < PollConst.MIN_OPTION_COUNT
                || pollDto.getPollAnswers().size() > PollConst.MAX_OPTION_COUNT
        ) {
            return ResponseEntity.badRequest().body(PollConst.OUT_OF_OPTION_COUNT_BOUNDS);
        }

        for (PollAnswerDto pollAnswerDto : pollDto.getPollAnswers()) {
            String option = pollAnswerDto.getPollAnswer();
            if (option.isBlank()) {
                return ResponseEntity.badRequest().body(PollConst.POLL_OPTION_BLANK);
            }
            if (option.length() > PollConst.OPTION_CHARACTER_LIMIT) {
                return ResponseEntity.badRequest().body(PollConst.POLL_OPTION_TOO_LONG);
            }
            option = option.replaceAll("\\s+", " ").trim();
            pollAnswerDto.setPollAnswer(option);
        }
        if (pollDto.getPollAnswers().stream().map(PollAnswerDto::getPollAnswer).distinct().count() != pollDto.getPollAnswers().size()) {
            return ResponseEntity.badRequest().body(PollConst.POLL_OPTIONS_NOT_UNIQUE);
        }

        Poll poll = pollMapper.toEntity(pollDto);
        poll = pollRepository.save(poll);

        for (PollAnswer pollAnswer : poll.getPollAnswers()) {
            pollAnswer.setPoll(poll);
        }

        Event event = eventRepository.findById(eventId).get();
        if (event.getPolls() == null) {
            event.setPolls(new HashSet<>());
        }

        event.getPolls().add(poll);
        eventRepository.save(event);
        return ResponseEntity.status(201).body(newPollDto);
    }

    public ResponseEntity<?> getPoll(int eventId, int pollId) {
        if (pollRepository.findById(pollId).isEmpty()) {
            return ResponseEntity.status(404).body(String.format(PollConst.POLL_NOT_FOUND, pollId));
        }
        if(eventRepository.findById(eventId).isEmpty()) {
            return ResponseEntity.status(404).body(String.format(PollConst.EVENT_NOT_FOUND, eventId));
        }
        if (!eventRepository.findById(eventId).get().getPolls().contains(pollRepository.findById(pollId).get()))
            return ResponseEntity.badRequest().body(String.format(PollConst.EVENT_AND_POLL_NOT_CORRELATED, eventId, pollId));
        return ResponseEntity.ok(pollMapper.toDto(pollRepository.findById(pollId).get()));
    }

    // todo needs to be checked
    public ResponseEntity<Set<PollToDisplayOnListDto>> listAllPollsForEvent(int eventId) {
        if (eventRepository.findById(eventId).isPresent()) {
            Event event = eventRepository.findById(eventId).get();
            Set<Poll> pollSet = event.getPolls();
            return ResponseEntity.ok(pollToDisplayOnListDtoMapper.convert(pollSet));
        }
        return ResponseEntity.notFound().build();
    }

    public ResponseEntity<PollAnswerDto> insertNewPollAnswer(PollAnswerDto pollAnswerDto) {
        return null;
    }

    public Boolean employeeOnLists(Poll poll, Integer employeeId) {
//        int id = employee.getId();
        return poll.getPollAnswers().stream()
                .flatMap(pollAnswer -> pollAnswer.getEmployee().stream())
                .anyMatch(employee -> employeeId.equals(employee.getId()));
    }
}


