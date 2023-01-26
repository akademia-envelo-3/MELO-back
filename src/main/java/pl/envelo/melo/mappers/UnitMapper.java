package pl.envelo.melo.mappers;

import org.mapstruct.Mapper;
import pl.envelo.melo.domain.unit.Unit;
import pl.envelo.melo.domain.unit.dto.UnitDto;

@Mapper(componentModel = "spring")
public interface UnitMapper extends EntityMapper<UnitDto, Unit> {
    default Unit map(Integer unitId) {
        Unit unit = new Unit();
        //fixme add UnitMapper
        unit.setId(unitId);
        return unit;
    }
}
