package pl.envelo.melo.domain.comment.dto;

import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentDto {
    @Size(min = 1, max = 2000, message = "Commentary must contain from 1 to 2000 chars")
    private String content;
}
