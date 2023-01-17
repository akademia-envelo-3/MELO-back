package pl.envelo.melo.mappers;

import lombok.AllArgsConstructor;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;
import pl.envelo.melo.authorization.employee.Employee;
import pl.envelo.melo.authorization.employee.EmployeeRepository;
import pl.envelo.melo.domain.category.CategoryRepository;
import pl.envelo.melo.domain.event.Event;
import pl.envelo.melo.domain.event.dto.NewEventDto;
import pl.envelo.melo.domain.unit.Unit;
import pl.envelo.melo.domain.unit.UnitRepository;

@Mapper(componentModel = "spring", uses = {HashtagMapper.class})
public abstract class NewEventMapper {
    @Autowired
    EmployeeRepository employeeRepository;
    @Autowired
    UnitRepository unitRepository;
    @Autowired
    CategoryRepository categoryRepository;

    public abstract Event convert(NewEventDto newEventDto);

    @AfterMapping
    public void updateResult(NewEventDto newEventDto, @MappingTarget Event event) {


    }
}