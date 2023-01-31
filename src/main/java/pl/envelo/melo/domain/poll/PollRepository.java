package pl.envelo.melo.domain.poll;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;
import pl.envelo.melo.domain.event.Event;
import pl.envelo.melo.domain.poll.dto.PollToDisplayOnListDto;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface PollRepository extends JpaRepository<Poll, Integer> {
//    public Optional<Event> findByIdAndFetchPollsEagerly(int eventId);

}
