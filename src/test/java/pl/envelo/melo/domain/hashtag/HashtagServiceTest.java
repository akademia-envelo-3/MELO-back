package pl.envelo.melo.domain.hashtag;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pl.envelo.melo.mappers.HashtagMapper;
import pl.envelo.melo.utils.SQLUtils;

import javax.sql.DataSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
class HashtagServiceTest {
    @Autowired
    HashtagRepository hashtagRepository;
    @Autowired
    HashtagService hashtagService;
    @Autowired
    HashtagMapper hashtagMapper;
    @Autowired
    DataSource dataSource;

    @BeforeEach
    void setUp() {
        SQLUtils.clearDb(dataSource);
    }

    @Test
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
        assertEquals(2, hashtag1.getGlobalUsageCount());
        //Incorrect request
        assertEquals(HttpStatus.valueOf(404), hashtagService.incrementHashtagGlobalCount(200).getStatusCode());

    }

    @Test
    void insertNewHashtag() {
        //TODO recreate test
        Hashtag hashtag = new Hashtag();
        hashtag.setId(1);
        hashtag.setContent("Coffe");
        hashtag.setGlobalUsageCount(1);
        hashtagRepository.save(hashtag);
//        ResponseEntity<HashtagDto> responseHashtag = hashtagService.insertNewHashtag(hashtagMapper.toDto(hashtag));
//        assertEquals(HttpStatus.OK, responseHashtag.getStatusCode());
//        Hashtag hashtag1 = hashtagMapper.toEntity(responseHashtag.getBody());
//        assertEquals(2,hashtagRepository.findByContent(responseHashtag.getBody().getContent()).get().getGlobalUsageCount());
//        HashtagDto hashtagDto = new HashtagDto();
//        hashtagDto.setContent("Game");
//        responseHashtag = hashtagService.insertNewHashtag(hashtagDto);
//        assertEquals(HttpStatus.OK, responseHashtag.getStatusCode());
//        assertTrue(hashtagRepository.existsByContent("Game"));

    }
}