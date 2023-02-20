package pl.envelo.melo.mappers;

import org.mapstruct.AfterMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import pl.envelo.melo.domain.event.EventRepository;
import pl.envelo.melo.domain.notification.Notification;
import pl.envelo.melo.domain.notification.dto.EventNotificationDto;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring", uses = EventRepository.class)
public interface EventNotificationMapper extends EntityMapper<EventNotificationDto, Notification> {

    @AfterMapping
    default void updateResult(EventNotificationDto eventNotificationDto, @MappingTarget Notification notification, @Context EventRepository eventRepository) {
        notification.setContent(eventNotificationDto.getContent());
        notification.setNotificationType(eventNotificationDto.getType());
        notification.setTimestamp(LocalDateTime.now());
        notification.setEvent(eventRepository.findById(eventNotificationDto.getEventId()).get());
    }

}
