package pl.envelo.melo.domain.attachment.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;
import pl.envelo.melo.domain.attachment.AttachmentConst;
import pl.envelo.melo.domain.attachment.AttachmentType;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttachmentDto {

    @NotBlank(message = AttachmentConst.INVALID_NAME)
    @Max(AttachmentConst.MAX_ATTACHMENT_NAME_LENGTH)
    private String name;

    @URL(message = AttachmentConst.INVALID_URL)
    private String attachmentUrl;

    @NotNull(message = AttachmentConst.INVALID_TYPE)
    private AttachmentType attachmentType;
}
