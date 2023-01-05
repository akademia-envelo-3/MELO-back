package pl.envelo.melo.domain.notification;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
}
