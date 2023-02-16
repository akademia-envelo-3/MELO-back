package pl.envelo.melo.domain.poll;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.envelo.melo.authorization.AuthorizationService;
import pl.envelo.melo.authorization.employee.Employee;
import pl.envelo.melo.authorization.employee.EmployeeRepository;
import pl.envelo.melo.authorization.employee.EmployeeService;
import pl.envelo.melo.domain.event.Event;
import pl.envelo.melo.domain.event.EventRepository;
import pl.envelo.melo.domain.poll.dto.*;
import pl.envelo.melo.exceptions.EmployeeNotFound;
import pl.envelo.melo.mappers.*;

import java.security.Principal;
import java.util.*;

@Service
@AllArgsConstructor
public class PollService {
    private final PollRepository pollRepository;
    private final PollAnswerRepository pollAnswerRepository;
    private final EventRepository eventRepository;
    private final PollMapper pollMapper;
    private final PollResultMapper pollResultMapper;
    private final NewPollMapper newPollMapper;
    private final PollToDisplayOnListDtoMapper pollToDisplayOnListDtoMapper;
    private final EmployeeService employeeService;
    private final EmployeeMapper employeeMapper;
    private final EmployeeRepository employeeRepository;
    private final AuthorizationService authorizationService;


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

    public ResponseEntity<?> getPoll(int eventId, int pollId, Principal principal) {
        authorizationService.inflateUser(principal);
        Employee employee = employeeRepository.findByUserId(authorizationService.getUUID(principal)).orElseThrow(EmployeeNotFound::new);
        int employeeId = employee.getId();
        if (checkPollValidation(eventId, pollId).getStatusCode().is2xxSuccessful()) {
            Event event = eventRepository.findById(eventId).get();
            Poll poll = pollRepository.findById(pollId).get();
            if (event.getPolls().contains(poll) && poll.getPollAnswers().stream().flatMap(pollAnswer -> pollAnswer.getEmployee()
                    .stream()).anyMatch(e -> e.getId() == employeeId)) {
                return ResponseEntity.status(200).body(pollResultMapper.toDto(poll));
            }
            return ResponseEntity.ok(pollMapper.toDto(poll));
        } else return checkPollValidation(eventId, pollId);
    }

    public ResponseEntity<?> checkPollValidation(int eventId, int pollId) {
        if (pollRepository.findById(pollId).isEmpty()) {
            return ResponseEntity.status(404).body(String.format(PollConst.POLL_NOT_FOUND, pollId));
        }
        if (eventRepository.findById(eventId).isEmpty()) {
            return ResponseEntity.status(404).body(String.format(PollConst.EVENT_NOT_FOUND, eventId));
        }
        if (!eventRepository.findById(eventId).get().getPolls().contains(pollRepository.findById(pollId).get()))
            return ResponseEntity.badRequest().body(String.format(PollConst.EVENT_AND_POLL_NOT_CORRELATED, eventId, pollId));
        return ResponseEntity.status(200).build();
    }

    public ResponseEntity<Set<Poll>> listAllPollsForEvent(int eventId) {
        if (eventRepository.findById(eventId).isPresent()) {
            Event event = eventRepository.findById(eventId).get();
            Set<Poll> pollSet = event.getPolls();
            return ResponseEntity.ok(pollSet);
        }
        return ResponseEntity.notFound().build();
    }

    public ResponseEntity<?> insertNewPollAnswer(int eventId, PollSendResultDto pollSendResultDto, Principal principal) {
        authorizationService.inflateUser(principal);
        Employee employee = employeeRepository.findByUserId(authorizationService.getUUID(principal)).orElseThrow(EmployeeNotFound::new);
        int employeeId = employee.getId(); //employee token
        Poll poll;

        if (checkPollValidation(eventId, pollSendResultDto.getPollId()).getStatusCode().is2xxSuccessful()) {
            poll = pollRepository.findById(pollSendResultDto.getPollId()).get();
        } else return checkPollValidation(eventId, pollSendResultDto.getPollId());

        if (poll.getPollAnswers().stream().anyMatch(e ->
                e.getEmployee().stream().anyMatch(emp -> emp.getId() == employeeId))) {
            return ResponseEntity.status(400).body("Employee already voted in this Poll.");
        }

        if (pollSendResultDto.getPollAnswerId().isEmpty()) {
            return ResponseEntity.status(400).body("PollAnswer must have at least one value.");
        }

        if (pollSendResultDto.getPollAnswerId().size() > 1 && !poll.isMultichoice()) {
            return ResponseEntity.status(400).body("This Poll is not multichoice, you can only put 1 PollAnswer.");
        }

        Set<PollAnswer> pollAnswerSet = new HashSet<>();

        for (Integer pollAnswerId : pollSendResultDto.getPollAnswerId()) {
            PollAnswer pollAnswer = poll.getPollAnswers().stream()
                    .filter(pollAnswer1 -> pollAnswerId.equals(pollAnswer1.getId()))
                    .findFirst()
                    .orElse(null);
            if (pollAnswer == null) {
                return ResponseEntity.status(404).body(PollConst.POLL_ANSWER_NOT_ASSOCIATED_WITH_POLL);
            }
            pollAnswer.getEmployee().add(employee);
            pollAnswerSet.add(pollAnswer);
        }

        poll.getPollAnswers().addAll(pollAnswerSet);
        pollAnswerSet.forEach(pollAnswerRepository::save);

        return ResponseEntity.status(201).body(pollResultMapper.toDto(poll));
    }


    public Boolean employeeOnLists(Poll poll, Integer employeeId) {
        return poll.getPollAnswers().stream()
                .flatMap(pollAnswer -> pollAnswer.getEmployee().stream())
                .anyMatch(employee -> employeeId.equals(employee.getId()));
    }
}


