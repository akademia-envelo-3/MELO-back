package pl.envelo.melo.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.envelo.melo.domain.request.CategoryRequest;
import pl.envelo.melo.domain.request.dto.CategoryRequestDto;
import pl.envelo.melo.domain.request.dto.CategoryRequestToDisplayOnListDto;

@Mapper(componentModel = "spring")
public interface CategoryRequestMapper {
    @Mapping(ignore = true, target = "employee")
    CategoryRequestToDisplayOnListDto toDisplayOnListDto(CategoryRequest categoryRequest);
}
