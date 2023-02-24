package pl.envelo.melo.domain.event;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import pl.envelo.melo.authorization.employee.EmployeeRepository;
import pl.envelo.melo.authorization.user.UserRepository;
import pl.envelo.melo.domain.event.dto.NewEventDto;
import pl.envelo.melo.domain.location.LocationService;
import pl.envelo.melo.domain.notification.NotificationService;
import pl.envelo.melo.domain.notification.NotificationType;
import pl.envelo.melo.domain.notification.dto.EventNotificationDto;

@Component
@AllArgsConstructor
public class EditEventNotificationHandler {
    private final LocationService locationService;
    private final NotificationService notificationService;

    public ResponseEntity<?> editNotification(Event event, NewEventDto eventDto) {
        EventNotificationDto eventNotificationDto = new EventNotificationDto();
        eventNotificationDto.setEventId(event.getId());
        //FIXME determine message for notification

        if (!event.getStartTime().equals(eventDto.getStartTime()) || !event.getEndTime().equals(eventDto.getEndTime())) {
            return sendNotification(eventNotificationDto, NotificationType.EVENT_DATE_CHANGED);
        }
        if ((event.getLocation() == null && eventDto.getLocation() != null) || (event.getLocation() != null && eventDto.getLocation() == null)) {
            return sendNotification(eventNotificationDto, NotificationType.EVENT_LOCATION_CHANGED);
        }
        if ((event.getLocation() == null || eventDto.getLocation() == null) || (!event.getLocation().equals(locationService.insertOrGetLocation(eventDto.getLocation())))) {
            return sendNotification(eventNotificationDto, NotificationType.EVENT_LOCATION_CHANGED);
        }
        return sendNotification(eventNotificationDto, NotificationType.EVENT_UPDATED);
    }

    private ResponseEntity<?> sendNotification(EventNotificationDto eventNotificationDto, NotificationType notificationType) {
        eventNotificationDto.setType(notificationType);
        return notificationService.insertEventMembersNotification(eventNotificationDto, false);
    }


}
