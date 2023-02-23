package pl.envelo.melo.domain.comment.dto;

import jakarta.validation.constraints.Size;
import lombok.*;
import pl.envelo.melo.domain.comment.CommentConst;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentDto {
    @Size(min = 1, max = 2000, message = CommentConst.INVALID_CONTENT_LENGTH)
    private String content;
}
