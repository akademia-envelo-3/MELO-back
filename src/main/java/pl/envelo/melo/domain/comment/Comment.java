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
public class Comment {
    
    @Id
    @GeneratedValue
    private int id;
    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee author;
    @OneToMany
    private List<Attachment> attachments;
    private String content;
    private LocalDateTime timestamp;

}
