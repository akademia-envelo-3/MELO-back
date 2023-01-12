package pl.envelo.melo.validators;

import pl.envelo.melo.domain.event.Event;
import pl.envelo.melo.domain.event.EventType;
import pl.envelo.melo.domain.event.dto.NewEventDto;

import java.util.HashMap;
import java.util.Map;

public class EventValidator {
    public static Map<String, String> validateToEdit(Event event, NewEventDto eventDto){
        Map<String ,String> errors = new HashMap<>();
        if(!event.getType().equals(eventDto.getEventType()))
            errors.put(EventType.class.getName()+" error","You cannot edit eventType");
        else {
            if(event.getType().name().contains("LIMITED")){
                if(eventDto.getMemberLimit()<2){
                    errors.put("memberLimit"+" error","You cannot set memberLimit to less than 2");
                }
                if(eventDto.getMemberLimit()<event.getMembers().size()){
                    errors.put("memberLimit" + " error", "You cannot set memberLimit to less than number of accepted members. You need to remove members if you wish to proceed");
                }
            }}
        return errors;
    }
}
