package pl.envelo.melo.mappers;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import pl.envelo.melo.domain.notification.Notification;
import pl.envelo.melo.domain.notification.dto.RequestNotificationDto;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring")
public interface RequestNotificationMapper extends EntityMapper<RequestNotificationDto, Notification> {

    @AfterMapping
    default void updateResult(RequestNotificationDto requestNotificationDto, @MappingTarget Notification notification) {
        notification.setContent(requestNotificationDto.getReason());
        notification.setNotificationType(requestNotificationDto.getNotificationType());
        notification.setTimestamp(LocalDateTime.now());
    }

}