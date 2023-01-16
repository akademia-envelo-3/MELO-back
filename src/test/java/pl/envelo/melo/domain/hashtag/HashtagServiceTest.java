package pl.envelo.melo.domain.hashtag;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class HashtagServiceTest {

    @Autowired
    HashtagRepository hashtagRepository;
    @Autowired
    HashtagService hashtagService;
    @Test
    @Transactional
    void incrementHashtagGlobalCount() {
        Hashtag hashtag = new Hashtag();
        hashtag.setId(1);
        hashtag.setContent("Coffe");
        hashtag.setGlobalUsageCount(1);
        hashtagRepository.save(hashtag);

        ResponseEntity<?> testhashtag = hashtagService.incrementHashtagGlobalCount(1);
        //Correct request
        assertEquals(HttpStatus.OK, testhashtag.getStatusCode());
        Hashtag hashtag1 = (Hashtag) testhashtag.getBody();
        //Right value
        assertEquals(2,hashtag1.getGlobalUsageCount());
        //Incorrect request
        assertEquals(HttpStatus.valueOf(404), hashtagService.incrementHashtagGlobalCount(200).getStatusCode());
    }
}