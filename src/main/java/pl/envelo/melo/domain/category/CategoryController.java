package pl.envelo.melo.domain.category;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@AllArgsConstructor
@Tag(name = "Category Controller")
@RequestMapping("category")
public class CategoryController {
    private final CategoryService categoryService;

    @PreAuthorize("hasAuthority(@securityConfiguration.getAdminRole())")
    @PostMapping("")
    @Operation(summary = "Add new category",
            responses = {
                    @ApiResponse(responseCode = "200", description = "If given category name already existed, but its status was hidden=TRUE - changes it to FALSE and returns new status", content =
                    @Content(mediaType = "application/json", schema = @Schema(defaultValue = "true/false"))
                    ),
                    @ApiResponse(responseCode = "201", description = "Adds new category if given name is not present in database", content = {
                            @Content(mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = Category.class)))
                    }),
                    @ApiResponse(responseCode = "404", description = "If category already exists")
            })
    public ResponseEntity<?> addNewCategory(@RequestBody @Valid CategoryDto categoryDto) {
        return categoryService.insertNewCategory(categoryDto);
    }

    @PreAuthorize("hasAuthority(@securityConfiguration.getAdminRole())")
    @PostMapping("{id}")
    @Operation(summary = "Change category name with given ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Changes category name and returns new category", content =
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Category.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "When category name is already present in database"),
                    @ApiResponse(responseCode = "404", description = "If category with given ID is not present in database")
            })
    public ResponseEntity<?> editCategoryName(@PathVariable("id") int id, @RequestBody CategoryDto categoryDto) {
        return categoryService.editCategoryName(id, categoryDto);
    }

    @PreAuthorize("hasAnyAuthority(@securityConfiguration.getAdminRole(), @securityConfiguration.getEmployeeRole())")
    @GetMapping("{id}")
    @Operation(summary = "Get category with given ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Show category with given ID", content =
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Category.class))
                    ),
                    @ApiResponse(responseCode = "404", description = "Category with given ID does not exist in database")
            })
    public ResponseEntity<?> getCategory(@RequestParam("id") int id) {
        return categoryService.getCategory(id);
    }

    @PreAuthorize("hasAuthority(@securityConfiguration.getAdminRole())")
    @PatchMapping("{id}")
    @Operation(summary = "Change \"hidden\" status for category with given ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Changes 'hidden' status to opposite and returns new value", content =
                    @Content(mediaType = "application/json", schema = @Schema(defaultValue = "true/false"))
                    ),
                    @ApiResponse(responseCode = "404", description = "If category with given ID is not present in database")
            })
    public ResponseEntity<?> toggleCategoryVisibility(@PathVariable("id") int id) {
        return categoryService.changeStatusCategory(id);
    }

    //    @PatchMapping("{id}")
//    public ResponseEntity<?> activateCategory(@PathVariable("id") int id) {
//        return categoryService.changeStatusCategory(id);
//    }
    @PreAuthorize("hasAnyAuthority(@securityConfiguration.getAdminRole(), @securityConfiguration.getEmployeeRole())")
    @GetMapping("")
    @Operation(summary = "Show list of all categories", description = "tokenId: <br />0 - admin, <br />1 - employee",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Shows list of all categories", content =
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Category.class)))
            })
    public ResponseEntity<List<Category>> showAllCategory(Principal principal) {
        return categoryService.listAllCategory(principal);
    }
}
