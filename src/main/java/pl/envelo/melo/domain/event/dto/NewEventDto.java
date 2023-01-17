package pl.envelo.melo.domain.event.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import pl.envelo.melo.domain.attachment.Attachment;
import pl.envelo.melo.domain.attachment.dto.AttachmentDto;
import pl.envelo.melo.domain.event.EventType;
import pl.envelo.melo.domain.event.PeriodicType;
import pl.envelo.melo.domain.event.Theme;
import pl.envelo.melo.domain.location.Location;
import pl.envelo.melo.domain.location.dto.LocationDto;
import pl.envelo.melo.domain.poll.PollTemplate;
import pl.envelo.melo.domain.poll.dto.PollTemplateDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NewEventDto {

    @NotNull
    @Size(max = 255)
    private String name;
    @NotNull
    @Size(max = 4000)
    private String description;
    @DateTimeFormat(pattern = "")
    private LocalDateTime startTime;
    @DateTimeFormat(pattern = "")
    private LocalDateTime endTime;
    @NotNull
    private EventType eventType;
    @NotNull
    private int organizerId;
    @NotNull
    private PeriodicType periodicType;
    @Size(max = 100)
    private Set<String> hashtags;
    private int memberLimit;
    private Set<Integer> invitedMembers;
    private Set<Integer> unitIds;
    private LocationDto location;
    private Set<AttachmentDto> attachments;
    private AttachmentDto mainPhoto;
    private Integer categoryId;
    private Theme theme;

    public NewEventDto(String name, String description, LocalDateTime startTime, LocalDateTime endTime,
                        EventType eventType, int organizerId, PeriodicType periodicType){
        this.name = name;
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
        this.eventType = eventType;
        this.organizerId = organizerId;
        this.periodicType = periodicType;
    }
}
