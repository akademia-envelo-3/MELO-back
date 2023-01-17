package pl.envelo.melo.mappers;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import pl.envelo.melo.authorization.employee.Employee;
import pl.envelo.melo.authorization.employee.dto.EmployeeNameDto;
import pl.envelo.melo.domain.event.Event;
import pl.envelo.melo.domain.poll.PollTemplate;
import pl.envelo.melo.domain.poll.dto.PollTemplateDto;

import java.util.*;

@Mapper(componentModel = "spring")
public interface PollTemplateMapper {

    @InheritInverseConfiguration
    PollTemplateDto convert(PollTemplate pollTemplate);

}
