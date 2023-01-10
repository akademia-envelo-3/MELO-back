package pl.envelo.melo.domain.poll;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PollAnswerRepository extends JpaRepository<PollAnswer, Integer> {
}
