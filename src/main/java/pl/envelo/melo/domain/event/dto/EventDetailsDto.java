package pl.envelo.melo.domain.event.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.envelo.melo.authorization.employee.dto.EmployeeNameDto;
import pl.envelo.melo.domain.attachment.dto.AttachmentDto;
import pl.envelo.melo.domain.event.EventType;
import pl.envelo.melo.domain.event.PeriodicType;
import pl.envelo.melo.domain.event.Theme;
import pl.envelo.melo.domain.location.dto.LocationDto;
import pl.envelo.melo.domain.poll.dto.PollDto;
import pl.envelo.melo.domain.poll.dto.PollQuestionDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventDetailsDto {

    private String name;
    private String description;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private EventType eventType;
    private EmployeeNameDto organizer;
    private PeriodicType periodicType;
    private List<PollDto> polls;
    private Set<String> hashtags;
    private int memberLimit;
    private List<EmployeeNameDto> confirmedMembers;
    private LocationDto location;
    private Set<AttachmentDto> attachments;
    private AttachmentDto mainPhoto;
    private String category;
    private Theme theme;
    private List<PollQuestionDto> pollQuestion;

}
