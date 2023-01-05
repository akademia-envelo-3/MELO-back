package pl.envelo.melo.domain.event;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.envelo.melo.domain.unit.Unit;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @NotBlank
    private String name;
    @NotBlank
    private String description;
    @NotBlank
    private LocalDateTime startTime;
    @NotBlank
    private LocalDateTime endTime;
    @NotBlank
    @ManyToOne
    private Employee organizer;
    @NotBlank
    private EventType type;
    @ManyToMany
    private Set<Person> members;
    private PeriodicType periodicType;
    @ManyToMany
    private Set<Employee> invited;
    @ManyToMany
    private Set<Unit> units;
    @ManyToMany
    private Set<Hashtag> hashtags;
    private int memberLimit;
    @ManyToOne
    private Category category;
    @OneToMany
    private Set<Attachments> attachments;
    @OneToMany
    private List<Comment> comments;
    @OneToMany
    private Set<Poll> polls;
    @ManyToOne
    private Location location;
    @ManyToMany
    private Theme theme;

}
