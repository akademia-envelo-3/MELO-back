package pl.envelo.melo.domain.unit;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.envelo.melo.authorization.employee.Employee;
import pl.envelo.melo.domain.event.Event;

import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "units")
public class Unit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotBlank
    @Column(nullable = false)
    private String name;
    @Column(length = 4000)
    private String description;
    @OneToMany
    private List<Event> eventList;
    @ManyToMany
    private Set<Employee> members;
    @ManyToOne(optional = false)
    private Employee owner;

}
