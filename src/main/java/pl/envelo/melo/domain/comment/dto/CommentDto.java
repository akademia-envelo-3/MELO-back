package pl.envelo.melo.domain.comment.dto;

import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import pl.envelo.melo.domain.attachment.dto.AttachmentDto;
import pl.envelo.melo.domain.comment.CommentConst;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentDto {
    @Size(min = 1, max = 2000, message = CommentConst.INVALID_CONTENT_LENGTH)
    private String content;
}
