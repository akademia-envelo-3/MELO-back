package pl.envelo.melo.domain.comment;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.envelo.melo.domain.attachment.Attachment;
import pl.envelo.melo.domain.attachment.AttachmentRepository;
import pl.envelo.melo.domain.comment.dto.CommentDto;

@Service
public class CommentService {

    private CommentRepository commentRepository;
    private AttachmentRepository attachmentRepository;

    public CommentService(CommentRepository commentRepository, AttachmentRepository attachmentRepository){
        this.commentRepository = commentRepository;
        this.attachmentRepository = attachmentRepository;
    }

    public ResponseEntity<?> insertNewComment(CommentDto commentToSave){
        return ResponseEntity.status(HttpStatusCode.valueOf(500)).body("chuj wyszedł");
       // return null;
    }
}
