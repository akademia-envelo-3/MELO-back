package pl.envelo.melo.domain.event;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Integer> {
   // @EntityGraph(attributePaths = {"members", "mainPhoto", "hashtags", "organizer", "location", "polls","invited", "type","category", "organizer.user", "organizer.user.person"})
  //  Optional<Event> findById(Integer integer);

    //@EntityGraph(attributePaths = {"members", "mainPhoto"})
    List<Event> findAllByStartTimeAfterAndType(LocalDateTime localDateTime, EventType eventType);
}
