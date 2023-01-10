package pl.envelo.melo.domain.hashtag;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class HashtagService {
    private final HashtagRepository hashtagRepository;

    public ResponseEntity<HashtagDto> insertNewHashtag(HashtagDto hashtagDto) {
        return null;
    }

    public ResponseEntity<Hashtag> incrementHashtagGlobalCount(int id) {
        return null;
    }

    public ResponseEntity<Hashtag> decrementHashtagGlobalCount(int id) {
        return null;
    }

    public ResponseEntity<?> setHashtagHiddenFlag(int id, boolean hide) {
        return null;
    }

    public ResponseEntity<List<HashtagDto>> listAllHashtag() {
        return null;
    }
}
