package pl.envelo.melo.mappers;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import pl.envelo.melo.domain.attachment.Attachment;
import pl.envelo.melo.domain.attachment.dto.AttachmentDto;

@Mapper(componentModel = "spring")
public interface AttachmentMapper {
    Attachment convert(AttachmentDto attachmentDto);
    @InheritInverseConfiguration
    AttachmentDto convert(Attachment attachment);
}
