package pl.envelo.melo.domain.comment;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.envelo.melo.authorization.employee.Employee;
import pl.envelo.melo.authorization.employee.EmployeeRepository;
import pl.envelo.melo.domain.attachment.AttachmentRepository;
import pl.envelo.melo.domain.comment.dto.CommentDto;
import pl.envelo.melo.domain.event.Event;
import pl.envelo.melo.domain.event.EventRepository;
import pl.envelo.melo.mappers.CommentMapper;

import java.time.LocalDateTime;
import java.util.Objects;
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

        // Sprawdzam czy komentarz nie jest całkiem pusty - bez załącznika i tekstu.
        if(mappedComment.getContent().isEmpty() && Objects.isNull(mappedComment.getAttachments())) {
            return ResponseEntity.status(666).build();
        }

        Optional<Employee> EmployeeFromDb = employeeRepository.findById(Integer.parseInt(userIdFromJWT));
        Optional<Event> tmpEvent = eventRepository.findById(eventId);

        // Sprawdzam istnieje event i użytkownik i lecę z zapisem.
        if (EmployeeFromDb.isPresent() && tmpEvent.isPresent()) {
            mappedComment.setAuthor(EmployeeFromDb.get());
            mappedComment.setTimestamp(LocalDateTime.now());

            if(!Objects.isNull(commentToSave.getAttachments())) {
                attachmentRepository.saveAll(mappedComment.getAttachments());
            }

            Comment commentFromDb = commentRepository.save(mappedComment);
            tmpEvent.get().getComments().add(commentFromDb);
            eventRepository.save(tmpEvent.get());
            return ResponseEntity.ok(commentFromDb);
        }

        /// Taktyczna pięćsetka, żeby nikt nie wiedział co się zepsuło.
        return ResponseEntity.internalServerError().build();
    }
}
