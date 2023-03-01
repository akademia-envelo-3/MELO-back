package pl.envelo.melo.mappers;

import org.mapstruct.*;
import org.springframework.stereotype.Component;
import pl.envelo.melo.domain.attachment.Attachment;
import pl.envelo.melo.domain.comment.Comment;
import pl.envelo.melo.domain.comment.dto.CommentDto;
import pl.envelo.melo.domain.comment.dto.CommentToDisplayDto;

import java.util.Objects;
import java.util.stream.Collectors;

@Component
@Mapper(componentModel = "spring")
public interface CommentMapper {
    CommentDto convert(Comment comment);

    @InheritInverseConfiguration
    Comment convert(CommentDto commentDto);

    @Mapping(source = "author.user.person.firstName", target = "firstName")
    @Mapping(source = "author.user.person.lastName", target = "lastName")
    @Mapping(ignore = true, target = "attachments")
    CommentToDisplayDto convertToDisplayDto(Comment comment);

    @AfterMapping
    default void update(@MappingTarget CommentToDisplayDto commentToDisplayDto, Comment comment) {
        if (Objects.nonNull(comment.getAttachments()))
            commentToDisplayDto.setAttachments(comment.getAttachments().stream().map(Attachment::getAttachmentUrl).collect(Collectors.toList()));
    }
}

