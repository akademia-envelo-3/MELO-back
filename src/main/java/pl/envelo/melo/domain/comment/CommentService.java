package pl.envelo.melo.domain.comment;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.envelo.melo.authorization.employee.Employee;
import pl.envelo.melo.authorization.employee.EmployeeRepository;
import pl.envelo.melo.domain.attachment.Attachment;
import pl.envelo.melo.domain.attachment.AttachmentRepository;
import pl.envelo.melo.domain.comment.dto.CommentDto;
import pl.envelo.melo.domain.event.Event;
import pl.envelo.melo.domain.event.EventRepository;
import pl.envelo.melo.mappers.CommentMapper;

import java.time.LocalDateTime;
import java.util.Optional;

@Service

public class CommentService {


    private CommentRepository commentRepository;
    private AttachmentRepository attachmentRepository;
    private CommentMapper commentMapper;
    private EventRepository eventRepository;
    private EmployeeRepository employeeRepository;

    public CommentService(CommentRepository commentRepository, AttachmentRepository attachmentRepository, CommentMapper commentMapper, EventRepository eventRepository, EmployeeRepository employeeRepository) {
        this.commentRepository = commentRepository;
        this.attachmentRepository = attachmentRepository;
        this.commentMapper = commentMapper;
        this.eventRepository = eventRepository;
        this.employeeRepository = employeeRepository;
    }

    public ResponseEntity<Comment> insertNewComment(int eventId, CommentDto commentToSave) {
        String userIdFromJWT = "1";   ////Zaciągnij mordo z UserDetailsDto

        Comment mappedComment = commentMapper.convert(commentToSave);

        Optional<Employee> EmployeeFromDb = employeeRepository.findById(Integer.parseInt(userIdFromJWT));
        if (EmployeeFromDb.isPresent()) {
            mappedComment.setAuthor(EmployeeFromDb.get());
        }

        Optional<Event> tmpEvent = eventRepository.findById(eventId);
        if (tmpEvent.isPresent()) {
            mappedComment.setTimestamp(LocalDateTime.now());
            ////ATTACHMENT KURNA - MULTIPLEFILE - Attachment service coś coś

            attachmentRepository.saveAll(mappedComment.getAttachments());

            Comment commentFromDb = commentRepository.save(mappedComment);
            tmpEvent.get().getComments().add(commentFromDb);
            eventRepository.save(tmpEvent.get());
            return ResponseEntity.ok(commentFromDb);
        }
        return ResponseEntity.noContent().build();
    }
}
