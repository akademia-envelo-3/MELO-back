package pl.envelo.melo.domain.event;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    @Column(nullable = false, length = EventConst.MAX_DESCRIPTION_LENGTH)
    private String description;
    @NotNull
    @Column(nullable = false)
    private LocalDateTime startTime;
    @NotNull
    @Column(nullable = false)
    private LocalDateTime endTime;
    @NotNull
    @ManyToOne(optional = false, cascade = CascadeType.PERSIST)
    private Employee organizer;
    @NotNull
    @Column(nullable = false)
    @Enumerated
    private EventType type;
    @ManyToMany(cascade = CascadeType.PERSIST)
    private Set<Person> members;
    @Enumerated(EnumType.STRING)
    private PeriodicType periodicType;
    @ManyToMany(cascade = CascadeType.PERSIST)
    private Set<Employee> invited;
    @ManyToOne
    private Unit unit;
    @ManyToMany(cascade = CascadeType.PERSIST)
    private Set<Hashtag> hashtags;
    private Long memberLimit;
    @ManyToOne
    @Enumerated
    private Category category;
    @OneToMany(cascade = CascadeType.ALL)
    private Set<Attachment> attachments;
    @OneToOne(cascade = CascadeType.ALL)
    private Attachment mainPhoto;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Comment> comments;
    @OneToMany(cascade = CascadeType.ALL)
    private Set<Poll> polls;
    @ManyToOne(cascade = CascadeType.PERSIST)
    private Location location;
    @Column(nullable = false)
    private Theme theme;

}
