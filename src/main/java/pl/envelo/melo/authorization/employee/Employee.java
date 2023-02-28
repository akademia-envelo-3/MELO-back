package pl.envelo.melo.authorization.employee;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.envelo.melo.authorization.AppUser;
import pl.envelo.melo.authorization.user.User;
import pl.envelo.melo.domain.event.Event;
import pl.envelo.melo.domain.notification.Notification;
import pl.envelo.melo.domain.unit.Unit;

import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "employees")
public class Employee implements AppUser {
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
    @OneToOne(optional = false)
    @NotNull
    private User user;
    @OneToMany
    private Set<Notification> notificationsBox;

    public String getFirstName() {
        return user.getPerson().getFirstName();
    }

    public String getLastName() {
        return user.getPerson().getLastName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Employee employee)) return false;
        return getUser().equals(employee.getUser());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUser());
    }
}
