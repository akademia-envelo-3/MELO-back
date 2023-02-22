package pl.envelo.melo.domain.attachment.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;
import pl.envelo.melo.domain.attachment.AttachmentType;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttachmentDto {

    @NotBlank(message = "name of attachment can't be blank")
    @Max(255)
    private String name;

    @URL(message = "Wrong format of attachment URL")
    private String attachmentUrl;

    @NotNull(message = "temporary validation. attachmentType can't be null")
    private AttachmentType attachmentType;
}
