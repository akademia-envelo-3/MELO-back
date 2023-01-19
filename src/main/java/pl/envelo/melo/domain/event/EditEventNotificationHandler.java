package pl.envelo.melo.domain.event;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import pl.envelo.melo.authorization.employee.Employee;
import pl.envelo.melo.authorization.employee.EmployeeRepository;
import pl.envelo.melo.authorization.person.Person;
import pl.envelo.melo.authorization.user.User;
import pl.envelo.melo.authorization.user.UserRepository;
import pl.envelo.melo.domain.event.dto.NewEventDto;
import pl.envelo.melo.domain.location.LocationService;
import pl.envelo.melo.domain.notification.NotificationType;
import pl.envelo.melo.domain.notification.dto.EventNotificationDto;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component
@AllArgsConstructor
public class EditEventNotificationHandler {
    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;
    private final LocationService locationService;

    public List<EventNotificationDto> editNotification(Event event, NewEventDto eventDto) {

        if (!event.getStartTime().equals(eventDto.getStartTime()) || !event.getEndTime().equals(eventDto.getEndTime())) {
            return notificationList(event.getMembers(), event.getId(), NotificationType.EVENT_DATE_CHANGED);
        }
        if ((event.getLocation() == null && eventDto.getLocation() != null) || (event.getLocation() != null && eventDto.getLocation() == null)) {
            return notificationList(event.getMembers(), event.getId(), NotificationType.EVENT_LOCATION_CHANGED);
        }
        if ((event.getLocation() == null || eventDto.getLocation() == null) || (!event.getLocation().equals(locationService.insertOrGetLocation(eventDto.getLocation())))) {
            return notificationList(event.getMembers(), event.getId(), NotificationType.EVENT_LOCATION_CHANGED);
        }
        return notificationList(event.getMembers(), event.getId(), NotificationType.EVENT_UPDATED);
    }

    private List<EventNotificationDto> notificationList(Set<Person> persons, int eventId, NotificationType notificationType) {
        if (persons != null) {
            List<EventNotificationDto> notificationDtoList = new LinkedList<>();
            for (Person person : persons) {
                Optional<User> user = userRepository.findByPerson(person);
                if (user.isPresent()) {
                    Optional<Employee> employee = employeeRepository.findByUser(user.get());
                    EventNotificationDto notificationDto = new EventNotificationDto();
                    notificationDto.setEventId(eventId);
                    notificationDto.setType(notificationType);
                    //FIXME determine message for notification
                    notificationDto.setContent("");
                    employee.ifPresent(value -> notificationDto.setEmployeeId(value.getId()));
                    notificationDtoList.add(notificationDto);
                } else {
                    //FIXME send mail to guest
                }
            }
            return notificationDtoList;
        }
        return null;
    }

}
