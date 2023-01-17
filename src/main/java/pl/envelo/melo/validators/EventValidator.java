package pl.envelo.melo.validators;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import pl.envelo.melo.domain.category.CategoryRepository;
import pl.envelo.melo.domain.event.Event;
import pl.envelo.melo.domain.event.EventType;
import pl.envelo.melo.domain.event.dto.NewEventDto;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Component
@AllArgsConstructor
public class EventValidator {
    CategoryRepository categoryRepository;

    public Map<String, String> validateToEdit(Event event, NewEventDto eventDto) {
        Map<String, String> errors = new HashMap<>();
        if (event.getStartTime().compareTo(LocalDateTime.now()) <= 0) {
            errors.put("forbidden error", "You cannot edit archived event");
            return errors;
        }
        if (!event.getType().equals(eventDto.getEventType()))
            errors.put(EventType.class.getName() + " error", "You cannot edit eventType");
        else {
            if (event.getType().name().contains("LIMITED")) {
                if (eventDto.getMemberLimit() < 2) {
                    errors.put("memberLimit" + " error", "You cannot set memberLimit to less than 2");
                }
                if (eventDto.getMemberLimit() < event.getMembers().size()) {
                    errors.put("memberLimit" + " error", "You cannot set memberLimit to less than number of accepted members. You need to remove members if you wish to proceed");
                }
            }
        }
        if (eventDto.getStartTime() != null && event.getStartTime() != null) {
            if (eventDto.getStartTime().compareTo(event.getStartTime()) >= 0) {
                errors.put("endTime error", "You must set endTime to be after startTime");
            }
        }
        if (eventDto.getStartTime() == null) {
            errors.put("startTime error", "You must set startTime");
        } else {
            if (eventDto.getStartTime().compareTo(LocalDateTime.now()) <= 0) {
                errors.put("startTime error", "You cannot set startTime to past time");
            }
        }
        if (eventDto.getEndTime() == null) {
            errors.put("endTime error", "You must set endTime");
        }
        return errors;
    }
}
