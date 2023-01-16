package pl.envelo.melo.mappers;

import org.mapstruct.Mapper;
import pl.envelo.melo.domain.attachment.Attachment;
import pl.envelo.melo.domain.attachment.dto.AttachmentDto;

@Mapper
public interface AttachmentMapper {
    Attachment convert(AttachmentDto attachmentDto);
}
