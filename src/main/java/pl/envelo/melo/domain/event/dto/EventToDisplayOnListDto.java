package pl.envelo.melo.domain.event.dto;

import lombok.*;
import pl.envelo.melo.authorization.employee.Employee;
import pl.envelo.melo.domain.attachment.dto.AttachmentDto;
import pl.envelo.melo.domain.event.EventType;
import pl.envelo.melo.domain.event.PeriodicType;
import pl.envelo.melo.domain.event.Theme;
import pl.envelo.melo.domain.location.Location;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

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
