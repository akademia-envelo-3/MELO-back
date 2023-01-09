package pl.envelo.melo.domain.attachment;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.envelo.melo.domain.attachment.dto.AttachmentDto;

@RequiredArgsConstructor
@Service
public class AttachmentService {

    @Autowired
    AttachmentRepository attachmentRepository;

    public ResponseEntity<AttachmentDto> insertNewAttachment(AttachmentDto attachmentDto) {
        return null;
    }
}
