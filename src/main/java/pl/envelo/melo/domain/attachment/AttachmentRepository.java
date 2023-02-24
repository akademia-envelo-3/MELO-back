package pl.envelo.melo.domain.attachment;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AttachmentRepository extends JpaRepository<Attachment, Integer> {
    public Attachment findByName(String filename);
}
