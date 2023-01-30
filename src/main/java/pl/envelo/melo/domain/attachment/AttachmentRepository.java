package pl.envelo.melo.domain.attachment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface AttachmentRepository extends JpaRepository<Attachment, Integer> {
    public boolean findByName (String filename);
}
