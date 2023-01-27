package pl.envelo.melo.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.envelo.melo.domain.unit.Unit;
import pl.envelo.melo.domain.unit.dto.UnitToDisplayOnListDto;

import java.util.Objects;

@Mapper(componentModel = "spring")
public interface UnitMapper {

    default Unit map(Integer unitId) {
        if(Objects.isNull(unitId))
            return null;
        Unit unit = new Unit();
        //fixme add UnitMapper
        unit.setId(unitId);
        return unit;
    }
    @Mapping(source = "id", target = "unitId")
    UnitToDisplayOnListDto convert(Unit unit);

}
