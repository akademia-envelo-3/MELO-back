package pl.envelo.melo.mappers;

import org.aspectj.lang.annotation.After;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import pl.envelo.melo.domain.notification.Notification;
import pl.envelo.melo.domain.notification.dto.NotificationDto;
import pl.envelo.melo.domain.poll.PollAnswer;
import pl.envelo.melo.domain.poll.dto.PollAnswerResultDto;

import java.util.List;
import java.util.Objects;

@Mapper(componentModel = "spring")
public interface NotificationMapper extends EntityMapper<NotificationDto, Notification> {

    @AfterMapping
    default void update(Notification notification, @MappingTarget NotificationDto notificationDto) {
        if(Objects.nonNull(notification.getEvent())) {
            notificationDto.setEventId(notification.getEvent().getId());
        }
        if(Objects.nonNull(notification.getUnit())) {
            notificationDto.setUnitId(notification.getUnit().getId());
        }
    }

    List<NotificationDto> toDto(List<Notification> E);

}
