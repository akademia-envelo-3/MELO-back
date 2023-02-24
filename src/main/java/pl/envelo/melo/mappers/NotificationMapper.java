package pl.envelo.melo.mappers;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import pl.envelo.melo.domain.notification.Notification;
import pl.envelo.melo.domain.notification.dto.NotificationDto;

import java.util.List;
import java.util.Objects;

@Mapper(componentModel = "spring")
public interface NotificationMapper extends EntityMapper<NotificationDto, Notification> {

    @AfterMapping
    default void update(Notification notification, @MappingTarget NotificationDto notificationDto) {
        if (Objects.nonNull(notification.getEvent())) {
            notificationDto.setEventName(notification.getEvent().getName());
        }
        if (Objects.nonNull(notification.getUnit())) {
            notificationDto.setUnitName(notification.getUnit().getName());
        }
    }

    List<NotificationDto> toDto(List<Notification> E);

}
