package pl.envelo.melo.domain.attachment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import pl.envelo.melo.domain.attachment.dto.AttachmentDto;

@RestController
public class AttachmentController {

    @Autowired
    AttachmentService attachmentService;

    public ResponseEntity<AttachmentDto> addNewAttachment(AttachmentDto attachmentDto) {
        return null;
    }
}
