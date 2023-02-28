package pl.envelo.melo.mappers;

import org.mapstruct.Mapper;
import pl.envelo.melo.domain.hashtag.Hashtag;
import pl.envelo.melo.domain.hashtag.HashtagDto;
import pl.envelo.melo.domain.poll.PollAnswer;
import pl.envelo.melo.domain.poll.dto.PollAnswerDto;

import java.util.Set;

@Mapper(componentModel = "spring")

public interface HashtagMapper extends EntityMapper<HashtagDto, Hashtag> {

    HashtagDto toDto(Hashtag hashtag);

    default String convertToString(Hashtag hashtag) {
        return hashtag.getContent();
    }
}
