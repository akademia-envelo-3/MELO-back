package pl.envelo.melo.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.envelo.melo.domain.request.CategoryRequest;
import pl.envelo.melo.domain.request.dto.CategoryRequestDto;
import pl.envelo.melo.domain.request.dto.CategoryRequestToDisplayOnListDto;

@Mapper(componentModel = "spring", uses = {EmployeeMapper.class})
public interface CategoryRequestMapper {
    CategoryRequestToDisplayOnListDto toDisplayOnListDto(CategoryRequest categoryRequest);
}
