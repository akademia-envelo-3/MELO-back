package pl.envelo.melo.domain.hashtag;


import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class HashtagController {

    private final HashtagService hashtagService;

    public ResponseEntity<List<HashtagDto>> showAllHashtags() {
        return null;
    }

    public ResponseEntity<Hashtag> hideHashtag(int id) {
        return null;
    }

    public ResponseEntity<Hashtag> unhideHashtag(int id) {
        return null;
    }

    @GetMapping("/admin/hashtagStatistic")
    public ResponseEntity<?> showHashtagsStatistic(){
        return hashtagService.listHashtagStatistic();
    }
}
