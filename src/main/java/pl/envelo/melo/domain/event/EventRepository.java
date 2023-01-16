package pl.envelo.melo.domain.event;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.envelo.melo.domain.comment.Comment;

@Repository
public interface EventRepository extends JpaRepository<Event,Integer> {
}
