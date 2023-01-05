package pl.envelo.melo.authorization.employee;


import com.sun.jdi.PrimitiveValue;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.envelo.melo.authorization.user.User;

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
    @OneToMany
    private Set<Comment> ownedComments;
    @ManyToMany
    private Set<Event> joinedEvents;
    @OneToMany
    private Set<Event> ownedEvents;
    @ManyToMany
    private Set<Unit> joinedUnits;
    @OneToMany
    private Set<Unit> ownedUnits;
    @OneToOne
    private User user;
    @OneToMany
    private Set<Notification> notificationsBox;

}
