package pl.envelo.melo.domain.comment.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentDto {

    @NotBlank(message = "comment cannot be empty")
    @Size(min = 1, max = 2000, message = "Commentary must contain from 1 to 2000 chars")
    private String content;
    @NonNull
    private int authorId;
    @Max(10)
    private List<AttachmentDto> attachments;
    @DateTimeFormat(pattern = "")
    private LocalDateTime timeStamp;
    @NotNull
    private int eventId;

}
