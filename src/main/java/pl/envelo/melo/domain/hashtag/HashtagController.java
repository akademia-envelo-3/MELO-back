package pl.envelo.melo.domain.hashtag;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class HashtagController {
    private final HashtagService hashtagService;

    public HashtagController(HashtagService hashtagService) {
        this.hashtagService = hashtagService;
    }

    public ResponseEntity<List<HashtagDto>> showAllHashtags() {
        return null;
    }

    public ResponseEntity<Hashtag> hideHashtag(int id) {
        return null;
    }

    public ResponseEntity<Hashtag> unhideHashtag(int id) {
        return null;
    }
}
