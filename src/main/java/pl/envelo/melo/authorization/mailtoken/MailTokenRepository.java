package pl.envelo.melo.authorization.mailtoken;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MailTokenRepository extends JpaRepository<MailToken,Integer> {
}
