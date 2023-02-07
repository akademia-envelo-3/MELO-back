package pl.envelo.melo.mappers;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import pl.envelo.melo.authorization.person.Person;
import pl.envelo.melo.authorization.person.dto.AddGuestToEventDto;

@Component
@Mapper(componentModel = "spring")
public interface AddGuestToEventMapper extends EntityMapper<AddGuestToEventDto, Person> {
}
