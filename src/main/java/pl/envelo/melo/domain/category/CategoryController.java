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
    public ResponseEntity<?> addNewCategory(@RequestBody CategoryDto categoryDto) {
        return categoryService.insertNewCategory(categoryDto);
    }

    @PostMapping("{id}")
    public ResponseEntity<?> editCategoryName(@PathVariable("id") int id, @RequestBody CategoryDto categoryDto) {
        return categoryService.editCategoryName(id, categoryDto);
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getCategory(@RequestParam("id") int id) {
        return categoryService.getCategory(id);
    }


    @PatchMapping("{id}")
    public ResponseEntity<?> toggleCategoryVisibility(@PathVariable("id") int id) {
        return categoryService.changeStatusCategory(id);
    }

//    @PatchMapping("{id}")
//    public ResponseEntity<?> activateCategory(@PathVariable("id") int id) {
//        return categoryService.changeStatusCategory(id);
//    }

    @GetMapping("")
    public ResponseEntity<List<CategoryDto>> showAllCategory() {
        return categoryService.listAllCategory();
    }
}
