package pl.envelo.melo.domain.category;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    CategoryDto addNewCategory(CategoryDto categoryDto) {
        return null;
    }

    CategoryDto getCategory(int id) {
        return null;
    }

    CategoryDto hideCategory(int id) {
        return null;
    }

    CategoryDto activateCategory(int id) {
        return null;
    }

    List<CategoryDto> showAllCategory() {
        return null;
    }
}
