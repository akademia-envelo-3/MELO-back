package pl.envelo.melo.mappers;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import pl.envelo.melo.domain.hashtag.Hashtag;
import pl.envelo.melo.domain.hashtag.HashtagDto;
import pl.envelo.melo.domain.unit.Unit;

@Mapper(componentModel = "spring")
public interface UnitMapper {

//    HashtagDto convertHashtagToHashtagDto(Hashtag hashtag);
//    @InheritInverseConfiguration
//    Hashtag convertHashtagDtoToHashtag(HashtagDto hashtagDto);


    default Unit map(Integer unitId) {
        Unit unit = new Unit();
        //fixme ^
        unit.setId(unitId);
        return unit;
    }
//    @InheritInverseConfiguration
//    Integer getIdFromUnit(Unit unit);




}
