package pl.envelo.melo.domain.hashtag;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HashtagRepository extends JpaRepository<Hashtag, Integer> {

    Optional<Hashtag> findByContentIgnoreCase(String content);

    Optional<Hashtag> findByContent(String content);
    Hashtag getById(Integer id);

    boolean existsByContent(String content);
//    boolean isHidden(Integer id);

    //Hashtag findByContent(String content);
}
