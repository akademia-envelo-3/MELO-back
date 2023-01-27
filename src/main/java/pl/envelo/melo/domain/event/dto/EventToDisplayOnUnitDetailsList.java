package pl.envelo.melo.domain.event.dto;

import java.time.LocalDateTime;
import java.util.List;

public class EventToDisplayOnUnitDetailsList {
    String name;
    LocalDateTime startTime;
    LocalDateTime endTime;
    String category;
    List<String> hashtags;
    int eventId;
}
