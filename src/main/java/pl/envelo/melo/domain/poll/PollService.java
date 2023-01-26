package pl.envelo.melo.domain.poll;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.envelo.melo.domain.event.Event;
import pl.envelo.melo.domain.event.EventRepository;
import pl.envelo.melo.domain.poll.dto.PollAnswerDto;
import pl.envelo.melo.domain.poll.dto.PollDto;
import pl.envelo.melo.domain.poll.dto.PollTemplateToDisplayOnListDto;
import pl.envelo.melo.mappers.PollAnswerMapper;
import pl.envelo.melo.mappers.PollMapper;

import java.net.URI;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
@AllArgsConstructor
public class PollService {
    private PollRepository pollRepository;
    private PollAnswerRepository pollAnswerRepository;
    private EventRepository eventRepository;
    private PollMapper pollMapper;
    private PollAnswerMapper pollAnswerMapper;

    private static final int OPTION_CHARACTER_LIMIT = 255;
    private static final int MIN_OPTION_COUNT = 2;
    private static final int MAX_OPTION_COUNT = 10;
    private static final String POLL_RESOURCE = "events/%d/polls/%d";
    private static final String POLL_OPTION_TOO_LONG = "One of the poll options is exceeding limit of 255 characters";
    private static final String POLL_OPTION_BLANK = "Poll option must not be blank";
    private static final String OUT_OF_OPTION_COUNT_BOUNDS = "Poll must have minimum of 2 options and maximum of 10 options";
    private static final String EVENT_AND_POLL_NOT_CORRELATED = "Event with id %d and poll with id %d are not correlated";


    public ResponseEntity<List<Integer>> calculatePollResults(int pollId) {
        return null;
    }


    public ResponseEntity<?> insertNewPoll(PollDto pollDto, int eventId) {
        if (Objects.isNull(pollDto.getPollAnswers())
                || pollDto.getPollAnswers().size() < MIN_OPTION_COUNT
                || pollDto.getPollAnswers().size() > MAX_OPTION_COUNT
        ) {
            return ResponseEntity.badRequest().body(OUT_OF_OPTION_COUNT_BOUNDS);
        }
        for (PollAnswerDto pollAnswerDto : pollDto.getPollAnswers()) {
            String option = pollAnswerDto.getPollAnswer();
            if (option.isBlank()) {
                return ResponseEntity.badRequest().body(POLL_OPTION_BLANK);
            }
            if (option.length() > OPTION_CHARACTER_LIMIT) {
                return ResponseEntity.badRequest().body(POLL_OPTION_TOO_LONG);
            }
        }
        if (eventRepository.findById(eventId).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Poll poll = pollMapper.toEntity(pollDto);
        poll = pollRepository.save(poll);

        for(PollAnswer pollAnswer : poll.getPollAnswers()) {
            pollAnswer.setPoll(poll);
        }


        Event event = eventRepository.findById(eventId).get();
        if(event.getPolls() == null) {
            event.setPolls(new HashSet<>());
        }

        event.getPolls().add(poll);
        eventRepository.save(event);
        return ResponseEntity.created(URI.create(String.format(POLL_RESOURCE, eventId, poll.getId()))).build();
    }

    public ResponseEntity<?> getPoll(int eventId, int pollId) {
        if (pollRepository.findById(pollId).isEmpty() || eventRepository.findById(eventId).isEmpty())
            return ResponseEntity.notFound().build();
        if (!eventRepository.findById(eventId).get().getPolls().contains(pollRepository.findById(pollId).get()))
            return ResponseEntity.status(HttpStatusCode.valueOf(403)).body(String.format(EVENT_AND_POLL_NOT_CORRELATED,eventId, pollId));
        return ResponseEntity.ok(pollMapper.toDto(pollRepository.findById(pollId).get()));
    }

    public ResponseEntity<List<PollTemplateToDisplayOnListDto>> listAllPollsForEvent(int eventId) {
        return null;
    }

    public ResponseEntity<PollAnswerDto> insertNewPollAnswer(PollAnswerDto pollAnswerDto) {
        return null;
    }
}
