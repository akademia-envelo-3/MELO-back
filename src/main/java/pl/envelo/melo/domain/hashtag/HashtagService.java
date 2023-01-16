package pl.envelo.melo.domain.hashtag;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class HashtagService {
    private final HashtagRepository hashtagRepository;

    public ResponseEntity<HashtagDto> insertNewHashtag(HashtagDto hashtagDto) {
        return null;
    }

    public ResponseEntity<?> incrementHashtagGlobalCount(int id) {

        if (hashtagRepository.existsById(id)){
            Hashtag hashtag = hashtagRepository.getById(id);
            hashtag.setGlobalUsageCount(hashtag.getGlobalUsageCount()+1);
            hashtagRepository.save(hashtag);
            return ResponseEntity.ok(hashtag);
        }
        else {
            return ResponseEntity.status(404).body("Hashtag by this ID do not exist");
        }
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
