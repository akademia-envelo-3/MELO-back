package pl.envelo.melo.domain.event;

import lombok.AllArgsConstructor;
import pl.envelo.melo.authorization.employee.Employee;
import pl.envelo.melo.authorization.employee.EmployeeRepository;
import pl.envelo.melo.authorization.person.Person;
import pl.envelo.melo.authorization.user.User;
import pl.envelo.melo.authorization.user.UserRepository;
import pl.envelo.melo.domain.event.dto.NewEventDto;
import pl.envelo.melo.domain.notification.NotificationType;
import pl.envelo.melo.domain.notification.dto.EventNotificationDto;
import pl.envelo.melo.mappers.LocationMapper;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@AllArgsConstructor
public class EditEventNotificationHandler {
    private static EmployeeRepository employeeRepository;
    private static UserRepository userRepository;
    private static LocationMapper locationMapper;

    public static List<EventNotificationDto> editNotification(Event event, NewEventDto eventDto) {
        if (!event.getStartTime().equals(eventDto.getStartTime()) || !event.getEndTime().equals(eventDto.getEndTime())) {
            return notificationList(event.getMembers(),event.getId(),NotificationType.EVENT_DATE_CHANGED);
        }
        if (!event.getLocation().equals(locationMapper.convert(eventDto.getLocation()))) {
            return notificationList(event.getMembers(), event.getId(), NotificationType.EVENT_LOCATION_CHANGED);
        }
        return notificationList(event.getMembers(), event.getId(), NotificationType.EVENT_UPDATED);
    }

    private static List<EventNotificationDto> notificationList(Set<Person> persons, int eventId, NotificationType notificationType) {
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
