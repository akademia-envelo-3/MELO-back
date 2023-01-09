package pl.envelo.melo.domain.poll;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.envelo.melo.domain.poll.dto.PollAnswerDto;
import pl.envelo.melo.domain.poll.dto.PollTemplateDto;
import pl.envelo.melo.domain.poll.dto.PollTemplateToDisplayOnListDto;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PollService {
    private PollTemplateRepository pollTemplateRepository;
    private PollRepository pollRepository;
    private PollAnswerRepository pollAnswerRepository;

    public ResponseEntity<List<Integer>> calculatePollResults(int pollId) {
        return null;
    }

    public ResponseEntity<PollTemplateDto> insertNewPollTemplate(PollTemplateDto pollTemplateDto) {
        return null;
    }

    public ResponseEntity<PollTemplateDto> getPollTemplate(int pollId) {
        return null;
    }

    public ResponseEntity<List<PollTemplateToDisplayOnListDto>> listAllPollsForEvent(int eventId) {
        return null;
    }

    public ResponseEntity<PollAnswerDto> insertNewPollAnswer(PollAnswerDto pollAnswerDto) {
        return null;
    }
}
