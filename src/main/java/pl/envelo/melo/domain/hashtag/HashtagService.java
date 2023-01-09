package pl.envelo.melo.domain.hashtag;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HashtagService {
    private final HashtagRepository hashtagRepository;

    public HashtagService(HashtagRepository hashtagRepository) {
        this.hashtagRepository = hashtagRepository;
    }

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
