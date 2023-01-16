package pl.envelo.melo.mappers;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import pl.envelo.melo.domain.hashtag.Hashtag;
import pl.envelo.melo.domain.hashtag.HashtagDto;

@Mapper(componentModel = "spring")
public interface HashtagMapper {

    Hashtag convert(HashtagDto hashtagDto);
    @InheritInverseConfiguration
    HashtagDto convert(Hashtag hashtag);
}
