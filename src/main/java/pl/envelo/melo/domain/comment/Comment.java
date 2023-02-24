package pl.envelo.melo.domain.comment;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.envelo.melo.authorization.employee.Employee;
import pl.envelo.melo.domain.attachment.Attachment;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue
    private int id;
    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee author;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Attachment> attachments;
    @Column(length = 2000)
    private String content;
    private LocalDateTime timestamp;
}
