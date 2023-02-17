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
public interface UnitNotificationMapper extends EntityMapper<UnitNotificationDto, Notification> {

//    @AfterMapping
//    default void update(Notification notification, @MappingTarget UnitNotificationDto unitNotificationDto) {
//        unitNotificationDto.setUnitId(notification.getUnit().getId());
//    }

    @AfterMapping
    default void update(UnitNotificationDto unitNotificationDto, @MappingTarget Notification notification, @Context UnitRepository unitRepository) {
        notification.setContent("");
        notification.setNotificationType(unitNotificationDto.getNotificationType());
        notification.setTimestamp(LocalDateTime.now());
        System.out.println("TYPIE@##################### " +notification.getTimestamp());
        notification.setUnit(unitRepository.findById(unitNotificationDto.getUnitId()).get());
    }

}
