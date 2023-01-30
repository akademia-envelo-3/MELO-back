package pl.envelo.melo.domain.event.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
@Getter
@Setter
@NoArgsConstructor
public class EventToDisplayOnUnitDetailsList {
    String name;
    LocalDateTime startTime;
    LocalDateTime endTime;
    String category;
    List<String> hashtags;
    int eventId;
}
