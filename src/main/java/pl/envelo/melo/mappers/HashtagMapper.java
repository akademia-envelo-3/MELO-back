package pl.envelo.melo.mappers;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.envelo.melo.domain.attachment.Attachment;
import pl.envelo.melo.domain.attachment.dto.AttachmentDto;
import pl.envelo.melo.domain.hashtag.Hashtag;
import pl.envelo.melo.domain.hashtag.HashtagDto;

@Mapper(componentModel = "spring")
public interface HashtagMapper {


    Hashtag convert(HashtagDto hashtagDto);
    @InheritInverseConfiguration
    HashtagDto convert(Hashtag hashtag);
    
    default String  convertToString(Hashtag hashtag){
        return hashtag.getContent();
    }
}
