package pl.envelo.melo.mappers;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import pl.envelo.melo.domain.unit.Unit;
import pl.envelo.melo.domain.unit.dto.UnitDetailsDto;

@Mapper(componentModel = "spring", uses = {EmployeeMapper.class, EventMapper.class})
public interface UnitDetailsMapper {
    @Mapping(source = "eventList", target = "events")
    UnitDetailsDto convert(Unit unit);

    @AfterMapping
    default void updateResult(Unit unit, @MappingTarget UnitDetailsDto unitDetailsDto) {
        unitDetailsDto.setMemberCount(unit.getMembers().size());
    }
}
