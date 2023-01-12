package pl.envelo.melo.mappers;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import pl.envelo.melo.domain.comment.Comment;
import pl.envelo.melo.domain.comment.dto.CommentDto;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    CommentDto convert(Comment comment);

    @InheritInverseConfiguration
    Comment convert(CommentDto commentDto);
}

