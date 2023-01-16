package pl.envelo.melo.authorization.employee;



import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.envelo.melo.authorization.user.User;
import pl.envelo.melo.domain.comment.Comment;
import pl.envelo.melo.domain.event.Event;
import pl.envelo.melo.domain.notification.Notification;
import pl.envelo.melo.domain.unit.Unit;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @ManyToMany
    private Set<Event> joinedEvents;
    @OneToMany
    private Set<Event> ownedEvents;
    @ManyToMany
    private Set<Unit> joinedUnits;
    @OneToMany
    private Set<Unit> ownedUnits;
    @OneToOne
    @NotBlank
    private User user;
    @OneToMany
    private Set<Notification> notificationsBox;
}
