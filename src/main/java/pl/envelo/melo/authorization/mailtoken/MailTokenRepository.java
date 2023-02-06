package pl.envelo.melo.authorization.mailtoken;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;
@Repository
public interface MailTokenRepository extends JpaRepository<MailToken,UUID> {
}
