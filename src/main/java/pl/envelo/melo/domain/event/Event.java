package pl.envelo.melo.domain.event;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Event {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @NotNull
    private String name;
    @NotNull
    private String description;
    @NotNull
    private LocalDateTime startTime;
    @NotNull
    private LocalDateTime endTime;
    @NotNull @ManyToOne
    private Employee organizer;
    @NotNull @ManyToOne
    private EventType type;
    @ManyToMany
    private Set<Person> members;
    @ManyToOne
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
