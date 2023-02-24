package pl.envelo.melo.mappers;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import pl.envelo.melo.domain.attachment.Attachment;
import pl.envelo.melo.domain.attachment.dto.AttachmentDto;

@Component
@Mapper(componentModel = "spring")
public interface AttachmentMapper extends EntityMapper<AttachmentDto, Attachment> {

}
