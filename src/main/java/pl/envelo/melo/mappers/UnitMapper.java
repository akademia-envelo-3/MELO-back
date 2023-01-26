package pl.envelo.melo.mappers;

import org.mapstruct.Context;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.http.ResponseEntity;
import pl.envelo.melo.authorization.employee.Employee;
import pl.envelo.melo.authorization.employee.EmployeeService;
import pl.envelo.melo.authorization.employee.dto.EmployeeDto;
import pl.envelo.melo.domain.hashtag.Hashtag;
import pl.envelo.melo.domain.hashtag.HashtagDto;
import pl.envelo.melo.domain.unit.Unit;
import pl.envelo.melo.domain.unit.UnitService;
import pl.envelo.melo.domain.unit.UnitRepository;
import pl.envelo.melo.domain.unit.dto.UnitDto;

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
    UnitDto convert(Unit unit);

}
