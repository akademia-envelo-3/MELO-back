package pl.envelo.melo.domain.comment;


import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.envelo.melo.authorization.AuthorizationService;
import pl.envelo.melo.authorization.employee.Employee;
import pl.envelo.melo.authorization.employee.EmployeeRepository;
import pl.envelo.melo.domain.attachment.Attachment;
import pl.envelo.melo.domain.attachment.AttachmentService;
import pl.envelo.melo.domain.comment.dto.CommentDto;
import pl.envelo.melo.domain.event.Event;
import pl.envelo.melo.domain.event.EventRepository;
import pl.envelo.melo.exceptions.EmployeeNotFoundException;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional
public class CommentService {


    private CommentRepository commentRepository;
    private EventRepository eventRepository;
    private EmployeeRepository employeeRepository;
    private AttachmentService attachmentService;
    private AuthorizationService authorizationService;

    //@Transactional
    public ResponseEntity<?> insertNewComment(int eventId, CommentDto commentToSave, MultipartFile[] multipartFiles, Principal principal) {
        Employee employee = employeeRepository.findByUserId(authorizationService.getUUID(principal)).orElseThrow(EmployeeNotFoundException::new);
        /// Obsługa błędu - brak czegokolwiek - i kontentu i atachmentu
        if (Objects.isNull(commentToSave) && Objects.isNull(multipartFiles)) {
            return ResponseEntity.badRequest().body(CommentConst.COMMENT_EMPTY);
        }

        /// Stworzenie nowej encji
        Comment mappedComment = new Comment();

        /// Inicjalizuję pustą listę w encji na załączniki
        if (mappedComment.getAttachments() == null) {
            mappedComment.setAttachments(new ArrayList<>());
        }

       /* // Sprawdzam czy komentarz nie jest całkiem pusty - bez załącznika i tekstu.
        if(Objects.isNull(commentToSave.getContent())) {
            return ResponseEntity.status(666).build();
        }*/

        /// Tymczasowe rozwiązanie z parsowaniem sztywnego ID pracownika. Do edycji w momenci tokena.
        Optional<Event> tmpEvent = eventRepository.findById(eventId);

        // Sprawdzam istnieje event i użytkownik i lecę z zapisem.
        if (tmpEvent.isPresent()) {
            mappedComment.setAuthor(employee);
            mappedComment.setTimestamp(LocalDateTime.now());
            mappedComment.setContent(commentToSave.getContent());

            if (!Objects.isNull(multipartFiles)) {
                for (MultipartFile multipartFile : multipartFiles) {

                    Attachment attachmentFromServer = attachmentService.uploadFileAndSaveAsAttachment(multipartFile);
                    if (attachmentFromServer == null) {
                        return ResponseEntity.badRequest()
                                .body(CommentConst.ILLEGAL_ATTACHMENT_FORMAT);
                    }
                    if (Objects.isNull(mappedComment.getAttachments())) {
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