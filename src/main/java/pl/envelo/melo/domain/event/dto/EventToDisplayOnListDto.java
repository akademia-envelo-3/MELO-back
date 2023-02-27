package pl.envelo.melo.domain.event.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.envelo.melo.domain.attachment.dto.AttachmentDto;
import pl.envelo.melo.domain.event.Theme;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventToDisplayOnListDto {

    private String name;
    private String description;
    private LocalDateTime startTime;
    private int invitedMembersNumber;
    private AttachmentDto mainPhoto;
    private Theme theme;
    private int eventId;
}
