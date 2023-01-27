package pl.envelo.melo.domain.event;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Repository
public interface EventRepository extends JpaRepository<Event, Integer> {
    List<Event> findAllByStartTimeAfterAndType(LocalDateTime localDateTime, EventType eventType);
}
