package pl.envelo.melo.authorization.mailtoken;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.envelo.melo.domain.event.Event;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Repository
public interface MailTokenRepository extends JpaRepository<MailToken,UUID> {
    Optional<List<MailToken>> findAllByEvent(Event event);
}
