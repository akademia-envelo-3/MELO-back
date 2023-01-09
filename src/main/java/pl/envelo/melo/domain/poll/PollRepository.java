package pl.envelo.melo.domain.poll;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PollRepository extends JpaRepository<Poll, Integer> {
}
