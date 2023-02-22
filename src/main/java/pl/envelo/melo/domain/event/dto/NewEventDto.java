package pl.envelo.melo.domain.event.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import pl.envelo.melo.domain.event.EventType;
import pl.envelo.melo.domain.event.PeriodicType;
import pl.envelo.melo.domain.event.Theme;
import pl.envelo.melo.domain.hashtag.HashtagDto;
import pl.envelo.melo.domain.location.dto.LocationDto;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NewEventDto {

    @NotBlank
    @Size(max = 255)
    private String name;
    @NotBlank
    @Size(max = 4000)
    private String description;
    @DateTimeFormat(pattern = "")
    private LocalDateTime startTime;
    @DateTimeFormat(pattern = "")
    private LocalDateTime endTime;
    @NotNull(message = "Event type cant be null")
    private EventType eventType;
    @NotNull
    private int organizerId;
    @NotNull(message = "Periodic type can not be null")
    private PeriodicType periodicType;
    @Size(max = 100)
    private Set<HashtagDto> hashtags;
    private Long memberLimit;
    private Set<Integer> invitedMembers;
    private Integer unitId;
    private LocationDto location;
    private Integer categoryId;
    private Theme theme;

}
