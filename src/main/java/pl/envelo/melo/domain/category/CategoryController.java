package pl.envelo.melo.domain.category;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("category")
public class CategoryController {
    private final CategoryService categoryService;


    @PostMapping("")
    public ResponseEntity<CategoryDto> addNewCategory(CategoryDto categoryDto) {
        return categoryService.insertNewCategory(categoryDto);
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getCategory(@RequestParam("id") int id) {
        return categoryService.getCategory(id);
    }


    public ResponseEntity<?> hideCategory(int id) {
        return categoryService.changeStatusCategory(id);
    }

    public ResponseEntity<?> activateCategory(int id) {
        return categoryService.changeStatusCategory(id);
    }

    @GetMapping("")
    public ResponseEntity<List<CategoryDto>> showAllCategory() {
        return categoryService.listAllCategory();
    }
}
