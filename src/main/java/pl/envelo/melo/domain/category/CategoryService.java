package pl.envelo.melo.domain.category;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.envelo.melo.authorization.AuthFailed;
import pl.envelo.melo.authorization.AuthSucceded;
import pl.envelo.melo.authorization.AuthorizationService;
import pl.envelo.melo.authorization.admin.Admin;
import pl.envelo.melo.authorization.admin.AdminRepository;
import pl.envelo.melo.authorization.employee.Employee;
import pl.envelo.melo.authorization.employee.EmployeeRepository;
import pl.envelo.melo.domain.event.Event;
import pl.envelo.melo.mappers.CategoryMapper;

import java.security.Principal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CategoryService {
    @Autowired
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final AuthorizationService authorizationService;
    private final EmployeeRepository employeeRepository;
    private final AdminRepository adminRepository;


    public ResponseEntity<?> changeStatusCategory(int id) {
        Optional<Category> categoryOptional = categoryRepository.findById(id);
        if (categoryOptional.isPresent()) {
            Category category = categoryOptional.get();
            category.setHidden(!category.isHidden());
            return ResponseEntity.ok(categoryRepository.save(category).isHidden());
        } else return ResponseEntity.notFound().build();
    }

    public ResponseEntity<?> insertNewCategory(CategoryDto categoryDto) {
        Category categoryFromDatabase = findByName(categoryDto.getName());
        Category category = categoryMapper.toEntity(categoryDto);
        if (categoryFromDatabase != null) {
            if (categoryFromDatabase.isHidden()) {
                ResponseEntity<?> categoryWithSwappedStatus = changeStatusCategory(findByName(category.getName()).getId());
                return ResponseEntity.status(200).body(categoryWithSwappedStatus.getBody());
            } else {
                return ResponseEntity.status(404).body("This category is already visible in database.");
            }
        }
        category.setHidden(false);
        return ResponseEntity.status(201).body(categoryRepository.save(category));
    }

    public ResponseEntity<?> editCategoryName(int id, CategoryDto categoryDto) {
        Optional<Category> categoryOptional = categoryRepository.findById(id);
        if (categoryOptional.isPresent()) {
            if (findByName(categoryDto.getName()) != null) {
                return ResponseEntity.status(400).body("Category with given name already exists in database");
            }
            Category category = categoryOptional.get();
            category.setName(categoryDto.getName());
            return ResponseEntity.ok(categoryRepository.save(category));
        } else return ResponseEntity.status(404).body("Category with given ID does not exist in database");
    }

    public ResponseEntity<?> getCategory(int id) {
        Optional<Category> categoryOptional = categoryRepository.findById(id);
        if (categoryOptional.isPresent()) {
            Category category = categoryOptional.get();
            return ResponseEntity.ok(category);
        } else return ResponseEntity.status(404).body("Category with given ID does not exist in database");
    }

    public ResponseEntity<List<Category>> listAllCategory(Principal principal) {
        authorizationService.inflateUser(principal);
        Employee employee = employeeRepository.findByUserId(authorizationService.getUUID(principal)).orElse(null);
        Admin admin = adminRepository.findByUserId(authorizationService.getUUID(principal)).orElse(null);
        List<Category> listOfCategories = categoryRepository.findAll();
        if (Objects.nonNull(admin))
            return ResponseEntity.ok(listOfCategories.stream().toList());
        if(Objects.nonNull(employee))
           return ResponseEntity.ok(listOfCategories.stream().filter(category -> !category.isHidden()).toList());
        return ResponseEntity.status(403).build();
    }

    private Category findByName(String name) {
        return this.categoryRepository.findByName(name);
    }

}
