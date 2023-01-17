package pl.envelo.melo.domain.notification;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.envelo.melo.domain.event.Event;
import pl.envelo.melo.domain.unit.Unit;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Notification {
    @Id
    @GeneratedValue
    private int id;
    private String content;
    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;
    @ManyToOne
    @JoinColumn(name = "unit_id")
    private Unit unit;
    private NotificationType notificationType;
    private LocalDateTime timestamp;
    private boolean checked;

    @Override
    public String toString() {
        return "Notification{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", event=" + event +
                ", unit=" + unit +
                ", notificationType=" + notificationType +
                ", timestamp=" + timestamp +
                ", checked=" + checked +
                '}';
    }
}
