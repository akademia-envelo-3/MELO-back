package pl.envelo.melo.domain.poll;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.envelo.melo.domain.event.Event;
import pl.envelo.melo.domain.event.EventController;
import pl.envelo.melo.domain.event.EventRepository;
import pl.envelo.melo.domain.poll.dto.PollAnswerDto;
import pl.envelo.melo.domain.poll.dto.PollTemplateDto;
import pl.envelo.melo.domain.poll.dto.PollTemplateToDisplayOnListDto;
import pl.envelo.melo.mappers.PollTemplateMapper;

import java.net.URI;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PollService {
    private PollTemplateRepository pollTemplateRepository;
    private PollRepository pollRepository;
    private PollAnswerRepository pollAnswerRepository;
    private EventRepository eventRepository;
    private PollTemplateMapper pollTemplateMapper;

    private static final int OPTION_CHARACTER_LIMIT = 255;
    private static final int MIN_OPTION_COUNT = 2;
    private static final int MAX_OPTION_COUNT = 10;
    private static final String POLL_RESOURCE = "events/%d/polls/%d";
    private static final String POLL_OPTION_TOO_LONG = "One of the poll options is exceeding limit of 255 characters";
    private static final String POLL_OPTION_BLANK = "Poll option must not be blank";
    private static final String OUT_OF_OPTION_COUNT_BOUNDS = "Poll must have minimum of 2 options and maximum of 10 options";


    public ResponseEntity<List<Integer>> calculatePollResults(int pollId) {
        return null;
    }

    public ResponseEntity<?> insertNewPollTemplate(PollTemplateDto pollTemplateDto, int eventId) {
        if (Objects.isNull(pollTemplateDto.getPollOptions())
                || pollTemplateDto.getPollOptions().size() < MIN_OPTION_COUNT
                || pollTemplateDto.getPollOptions().size() > MAX_OPTION_COUNT
        ) {
            return ResponseEntity.badRequest().body(OUT_OF_OPTION_COUNT_BOUNDS);
        }
        for (String option : pollTemplateDto.getPollOptions()) {
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
        PollTemplate pollTemplate = pollTemplateMapper.convert(pollTemplateDto);
        pollTemplateRepository.save(pollTemplate);
        Poll poll = new Poll();
        poll.setPollTemplate(pollTemplate);
        Event event = eventRepository.findById(eventId).get();
        if (event.getPolls() == null) {
            event.setPolls(new HashSet<>());
        }
        event.getPolls().add(pollRepository.save(poll));
        eventRepository.save(event);
        return ResponseEntity.created(URI.create(String.format(POLL_RESOURCE, eventId, poll.getId()))).build();
    }

    public ResponseEntity<?> getPollTemplate(int eventId, int pollId) {
        if (pollRepository.findById(pollId).isEmpty() || eventRepository.findById(eventId).isEmpty())
            return ResponseEntity.notFound().build();
        if (!eventRepository.findById(eventId).get().getPolls().contains(pollRepository.findById(pollId).get()))
            return ResponseEntity.status(HttpStatusCode.valueOf(403)).build();
        return ResponseEntity.ok(pollTemplateMapper.convert(pollRepository.findById(pollId).get().getPollTemplate()));
    }

    public ResponseEntity<List<PollTemplateToDisplayOnListDto>> listAllPollsForEvent(int eventId) {
        return null;
    }

    public ResponseEntity<PollAnswerDto> insertNewPollAnswer(PollAnswerDto pollAnswerDto) {
        return null;
    }
}
