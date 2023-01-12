package pl.envelo.melo.domain.comment;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.envelo.melo.domain.attachment.Attachment;
import pl.envelo.melo.domain.attachment.AttachmentRepository;
import pl.envelo.melo.domain.comment.dto.CommentDto;
import pl.envelo.melo.mappers.CommentMapper;

import java.net.URI;
import java.util.Optional;


@Service
public class CommentService {

    private CommentRepository commentRepository;
    private AttachmentRepository attachmentRepository;
    private CommentMapper commentMapper;

    public CommentService(CommentRepository commentRepository, AttachmentRepository attachmentRepository, CommentMapper commentMapper){
        this.commentRepository = commentRepository;
        this.attachmentRepository = attachmentRepository;
        this.commentMapper = commentMapper;
    }

    public ResponseEntity<Comment> insertNewComment(CommentDto commentToSave){
        Comment mappedComment = commentMapper.convert(commentToSave);
        Comment createdCommentFromDb = commentRepository.save(mappedComment);
        return ResponseEntity.ok(createdCommentFromDb);
    }
}
