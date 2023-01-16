package pl.envelo.melo.domain.hashtag;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HashtagRepository extends JpaRepository<Hashtag, Integer> {
    Hashtag getById(Integer id);

    boolean existsByContent(String content);

    Hashtag findByContent(String content);
}
