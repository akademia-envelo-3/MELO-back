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
    int id;
    @NotNull
    String name;
    @NotNull
    String description;
    @NotNull
    LocalDateTime startTime;
    @NotNull
    LocalDateTime endTime;
    @NotNull @ManyToOne
    Employee organizer;
    @NotNull @ManyToOne
    EventType type;
    @ManyToMany
    Set<Person> members;
    @ManyToOne
    PeriodicType periodicType;
    @ManyToMany
    Set<Employee> invited;
    @ManyToMany
    Set<Unit> units;
    @ManyToMany
    Set<Hashtag> hashtags;
    int memberLimit;
    @ManyToOne
    Category category;
    @OneToMany
    Set<Attachments> attachments;
    @OneToMany
    List<Comment> comments;
    @OneToMany
    Set<Poll> polls;
    @ManyToOne
    Location location;
    @ManyToMany
    Theme theme;

}
