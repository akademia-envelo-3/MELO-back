package pl.envelo.melo.domain.unit;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.envelo.melo.domain.event.Event;

import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Unit {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @NotNull
    private String name;
    private String description;
    @ManyToMany
    private List<Event> eventList;
    @ManyToMany
    private Set<Employee> members;
    @NotNull @ManyToMany
    private Employee owner;
}
