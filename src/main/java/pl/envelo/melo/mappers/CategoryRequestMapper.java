package pl.envelo.melo.mappers;

import org.aspectj.lang.annotation.After;
import org.mapstruct.*;
import pl.envelo.melo.authorization.employee.EmployeeRepository;
import pl.envelo.melo.domain.request.CategoryRequest;
import pl.envelo.melo.domain.request.dto.CategoryRequestDto;
import pl.envelo.melo.domain.request.dto.CategoryRequestToDisplayOnListDto;
import pl.envelo.melo.exceptions.EmployeeNotFoundException;

@Mapper(componentModel = "spring", uses = {EmployeeMapper.class, EmployeeRepository.class})
public interface CategoryRequestMapper extends EntityMapper<CategoryRequestDto, CategoryRequest>{
    CategoryRequestToDisplayOnListDto toDisplayOnListDto(CategoryRequest categoryRequest);

    CategoryRequest toEntity(CategoryRequestDto categoryRequestDto, @Context EmployeeRepository employeeRepository);

    @AfterMapping
    default void toDtoUpdate(CategoryRequest categoryRequest, @MappingTarget CategoryRequestDto categoryRequestDto) {
        categoryRequestDto.setEmployeeId(categoryRequest.getEmployee().getId());
    }

    @AfterMapping
    default void toEntityUpdate(CategoryRequestDto categoryRequestDto, @MappingTarget CategoryRequest categoryRequest, @Context EmployeeRepository employeeRepository) {
        if(employeeRepository.findById(categoryRequestDto.getEmployeeId()).isPresent())
            categoryRequest.setEmployee(employeeRepository.findById(categoryRequestDto.getEmployeeId()).get());
        else throw new EmployeeNotFoundException();
        categoryRequest.setCategoryName(categoryRequest.getCategoryName().toLowerCase().trim());
    }

}
