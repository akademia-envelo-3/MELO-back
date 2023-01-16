package pl.envelo.melo.domain.attachment;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Data
public class Attachment {
    @Id
    @GeneratedValue
    private int id;
    private String name;
    private String attachmentUrl;
    private AttachmentType attachmentType;
}
