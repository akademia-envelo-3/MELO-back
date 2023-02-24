package pl.envelo.melo.domain.notification;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.envelo.melo.domain.event.Event;

import java.util.List;
import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {

    Optional<List<Notification>> findAllByEvent(Event event);
}
