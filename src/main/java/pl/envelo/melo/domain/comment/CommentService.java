package pl.envelo.melo.domain.comment;


import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.envelo.melo.authorization.employee.Employee;
import pl.envelo.melo.authorization.employee.EmployeeRepository;
import pl.envelo.melo.domain.attachment.Attachment;
import pl.envelo.melo.domain.attachment.AttachmentRepository;
import pl.envelo.melo.domain.attachment.AttachmentService;
import pl.envelo.melo.domain.comment.dto.CommentDto;
import pl.envelo.melo.domain.event.Event;
import pl.envelo.melo.domain.event.EventRepository;
import pl.envelo.melo.mappers.CommentMapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;

@Service
public class CommentService {


    private CommentRepository commentRepository;
    private AttachmentRepository attachmentRepository;
    private CommentMapper commentMapper;
    private EventRepository eventRepository;
    private EmployeeRepository employeeRepository;
    private AttachmentService attachmentService;

    public CommentService(CommentRepository commentRepository, AttachmentRepository attachmentRepository,
                          CommentMapper commentMapper, EventRepository eventRepository,
                          EmployeeRepository employeeRepository, AttachmentService attachmentService) {
        this.commentRepository = commentRepository;
        this.attachmentRepository = attachmentRepository;
        this.commentMapper = commentMapper;
        this.eventRepository = eventRepository;
        this.employeeRepository = employeeRepository;
        this.attachmentService = attachmentService;
    }

    @Transactional
    public ResponseEntity<?> insertNewComment(int eventId, CommentDto commentToSave, MultipartFile[] multipartFiles) {
        String userIdFromJWT = "1";   ////Zaciągnij mordo z UserDetailsDto

        /// Obsługa błędu - brak czegokolwiek - i kontentu i atachmentu
        if(Objects.isNull(commentToSave) && Objects.isNull(multipartFiles)) {
            return ResponseEntity.badRequest().body("Comment is empty. You must add CONTENT, ATTACHMENT or both together.");
        }

        /// Stworzenie nowej encji
        Comment mappedComment = new Comment();

        /// Inicjalizuję pustą listę w encji na załączniki
        if(mappedComment.getAttachments() == null) {
            mappedComment.setAttachments(new ArrayList<>());
        }

       /* // Sprawdzam czy komentarz nie jest całkiem pusty - bez załącznika i tekstu.
        if(Objects.isNull(commentToSave.getContent())) {
            return ResponseEntity.status(666).build();
        }*/

        /// Tymczasowe rozwiązanie z parsowaniem sztywnego ID pracownika. Do edycji w momenci tokena.
        Optional<Employee> EmployeeFromDb = employeeRepository.findById(Integer.parseInt(userIdFromJWT));
        Optional<Event> tmpEvent = eventRepository.findById(eventId);

        // Sprawdzam istnieje event i użytkownik i lecę z zapisem.
        if (EmployeeFromDb.isPresent() && tmpEvent.isPresent()) {
            mappedComment.setAuthor(EmployeeFromDb.get());
            mappedComment.setTimestamp(LocalDateTime.now());
            mappedComment.setContent(commentToSave.getContent());

            if(!Objects.isNull(multipartFiles)) {
                for (MultipartFile multipartFile : multipartFiles) {

                    Attachment attachmentFromServer = attachmentService.uploadFileAndSaveAsAttachment(multipartFile);
                    if (attachmentFromServer == null) {
                        return ResponseEntity.badRequest()
                                .body("Illegal format of attachment. WTF ARE U DOING?");
                    }
                    if(Objects.isNull(mappedComment.getAttachments())) {
                        mappedComment.setAttachments(new ArrayList<>());
                    }
                    mappedComment.getAttachments().add(attachmentFromServer);
                }
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