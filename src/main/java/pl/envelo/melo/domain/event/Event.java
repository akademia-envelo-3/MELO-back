package pl.envelo.melo.domain.event;

import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.envelo.melo.authorization.employee.Employee;
import pl.envelo.melo.authorization.person.Person;
import pl.envelo.melo.domain.attachment.Attachment;
import pl.envelo.melo.domain.category.Category;
import pl.envelo.melo.domain.comment.Comment;
import pl.envelo.melo.domain.hashtag.Hashtag;
import pl.envelo.melo.domain.location.Location;
import pl.envelo.melo.domain.poll.Poll;
import pl.envelo.melo.domain.unit.Unit;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotBlank
    @Column(nullable = false)
    private String name;
    @NotBlank
    @Column(nullable = false, length = 4000)
    private String description;
    @NotNull
    @Column(nullable = false)
    private LocalDateTime startTime;
    @NotNull
    @Column(nullable = false)
    private LocalDateTime endTime;
    @NotNull
    @ManyToOne(optional = false)
    private Employee organizer;
    @NotNull
    @Column(nullable = false)
    private EventType type;
    @ManyToMany
    private Set<Person> members;
    private PeriodicType periodicType;
    @ManyToMany
    private Set<Employee> invited;
    @ManyToOne
    private Unit unit;
    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    private Set<Hashtag> hashtags;
    private Long memberLimit;
    @ManyToOne
    private Category category;
    @OneToMany(cascade=CascadeType.ALL)
    private Set<Attachment> attachments;
    @OneToOne
    private Attachment mainPhoto;
    @OneToMany
    private List<Comment> comments;
    @OneToMany
    private Set<Poll> polls;
    @ManyToOne
    private Location location;
    @Column(nullable = false)
    private Theme theme;

}
