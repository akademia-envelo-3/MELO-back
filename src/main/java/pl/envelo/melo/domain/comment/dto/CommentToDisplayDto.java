package pl.envelo.melo.domain.comment.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CommentToDisplayDto {
    String firstName;
    String lastName;
    String content;
    List<String> attachments;
}
