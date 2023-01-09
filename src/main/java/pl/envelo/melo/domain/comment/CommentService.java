package pl.envelo.melo.domain.comment;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.envelo.melo.domain.attachment.Attachment;
import pl.envelo.melo.domain.comment.dto.CommentDto;

@Service
public class CommentService {

    private CommentRepository commentRepository;
    private AttachmentRepository attachmentRepository;

    public CommentService(CommentRepository commentRepository, AttachmentRepository attachmentRepository){
        this.commentRepository = commentRepository;
        this.attachmentRepository = attachmentRepository;
    }

    public ResponseEntity<Comment> insertNewComment(CommentDto commentToSave){
        return null;
    }
}
