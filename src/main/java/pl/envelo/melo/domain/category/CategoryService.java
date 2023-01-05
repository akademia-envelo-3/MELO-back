package pl.envelo.melo.domain.category;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category changeStatusCategory(int id) {
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
