package pl.envelo.melo.domain.category;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;


    public CategoryDto changeStatusCategory(int id) {
        return null;
    }

    public CategoryDto insertNewCategory(CategoryDto categoryDto) {
        return null;
    }

    public CategoryDto getCategory(int id) {
        return null;
    }

    public List<CategoryDto> listAllCategory() {
        return null;
    }
}
