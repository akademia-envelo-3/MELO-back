package pl.envelo.melo.domain.event.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.envelo.melo.domain.location.dto.LocationDto;

import java.time.LocalDateTime;
import java.util.List;
@Getter
@Setter
@NoArgsConstructor
public class EventToDisplayOnUnitDetailsList {
    private String name;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String category;
    private List<String> hashtags;
    private LocationDto location;
    private int eventId;
}
