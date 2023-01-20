package pl.envelo.melo.domain.comment;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.envelo.melo.authorization.employee.Employee;
import pl.envelo.melo.domain.attachment.Attachment;

import java.sql.Timestamp;
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
    @NotNull
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee author;
    @OneToMany
    @Max(10)
    private List<Attachment> attachments;
    @Column(nullable = false, length = 2000)
    private String content;
    @NotNull
    private LocalDateTime timestamp;
}
