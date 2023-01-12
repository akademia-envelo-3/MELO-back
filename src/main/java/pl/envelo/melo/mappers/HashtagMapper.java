package pl.envelo.melo.mappers;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.envelo.melo.domain.hashtag.Hashtag;

@Mapper(componentModel = "spring")
public interface HashtagMapper {
    @Mapping(target = "content")
    public Hashtag convert(String content);
}
