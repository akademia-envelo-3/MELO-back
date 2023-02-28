package pl.envelo.melo.mappers;

import org.mapstruct.AfterMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import pl.envelo.melo.domain.notification.Notification;
import pl.envelo.melo.domain.notification.dto.UnitNotificationDto;
import pl.envelo.melo.domain.unit.UnitRepository;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring", uses = UnitRepository.class)
public interface UnitNotificationMapper {

    Notification toEntity(UnitNotificationDto unitNotificationDto, @Context UnitRepository unitRepository);

    @AfterMapping
    default void updateResult(UnitNotificationDto unitNotificationDto, @MappingTarget Notification notification, @Context UnitRepository unitRepository) {
        notification.setContent(unitNotificationDto.getContent());
        notification.setNotificationType(unitNotificationDto.getNotificationType());
        notification.setTimestamp(LocalDateTime.now());
        notification.setUnit(unitRepository.findById(unitNotificationDto.getUnitId()).get());
    }

}
