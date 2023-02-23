package pl.envelo.melo.domain.event.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import pl.envelo.melo.domain.attachment.dto.AttachmentDto;
import pl.envelo.melo.domain.event.*;
import pl.envelo.melo.domain.hashtag.HashtagDto;
import pl.envelo.melo.domain.location.dto.LocationDto;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NewEventDto {

    @NotBlank(message = EventConst.BLANK_TITLE)
    @Size(max = EventConst.MAX_TITLE_LENGTH, message = EventConst.INVALID_TITLE)
    private String name;
    @NotBlank(message = EventConst.BLANK_DESCRIPTION)
    @Size(max = EventConst.MAX_DESCRIPTION_LENGTH, message = EventConst.INVALID_DESCRIPTION)
    private String description;
    @DateTimeFormat(pattern = "")
    private LocalDateTime startTime;
    @DateTimeFormat(pattern = "")
    private LocalDateTime endTime;
    @NotNull(message = EventConst.INVALID_EVENT_TYPE)
    private EventType eventType;
    @NotNull(message = EventConst.INVALID_ORGANIZER)
    private int organizerId;
    @NotNull(message = EventConst.INVALID_PERIODIC_TYPE)
    private PeriodicType periodicType;
    @Size(max = EventConst.MAX_HASHTAG_COUNT, message = EventConst.HASHTAG_LIMIT_REACHED)
    private Set<HashtagDto> hashtags;
    private Long memberLimit;
    private Set<Integer> invitedMembers;
    private Integer unitId;
    private LocationDto location;
    private Integer categoryId;
    private Theme theme;

}
