package pl.envelo.melo.mappers;

import org.mapstruct.Mapper;
import pl.envelo.melo.domain.category.Category;
import pl.envelo.melo.domain.category.CategoryDto;

@Mapper(componentModel = "spring")
public interface CategoryMapper extends EntityMapper<CategoryDto, Category> {
    default String convert(Category category) {
        if (category != null)
            return category.getName();
        return null;
    }
}
