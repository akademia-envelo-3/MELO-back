package pl.envelo.melo.domain.hashtag;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class HashtagService {
    private final HashtagRepository hashtagRepository;

    public HashtagDto insertNewHashtag(HashtagDto hashtagDto) {
        return null;
    }

    public Hashtag incrementHashtagGlobalCount(int id) {
        return null;
    }

    public Hashtag decrementHashtagGlobalCount(int id) {
        return null;
    }

    public void setHashtagHiddenFlag(int id, boolean hide) {
    }

    public List<HashtagDto> listAllHashtag() {
        return null;
    }
}
