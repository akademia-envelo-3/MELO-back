package pl.envelo.melo.domain.request;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.envelo.melo.domain.category.CategoryService;
import pl.envelo.melo.domain.notification.NotificationService;
import pl.envelo.melo.domain.request.dto.CategoryRequestDto;
import pl.envelo.melo.domain.request.dto.CategoryRequestToDisplayOnListDto;

import java.util.List;

@RequestMapping("/v1/categoryrequests")
@RestController
@AllArgsConstructor
public class CategoryRequestController {

    private final CategoryRequestService categoryRequestService;
    private final NotificationService notificationService;
    private final CategoryService categoryService;

    public ResponseEntity<CategoryRequest> addNewCategoryRequest(CategoryRequestDto categoryRequestDto) {
        return categoryRequestService.insertNewCategoryRequest(categoryRequestDto);
    }

    @PreAuthorize("hasAuthority(@securityConfiguration.getAdminRole())")
    @GetMapping
    public ResponseEntity<List<CategoryRequestToDisplayOnListDto>> showAllCategoryRequests(@RequestParam("resolved") boolean resolved) {
        return categoryRequestService.listCategoryRequests(resolved);
    }

    public ResponseEntity<?> checkIfCategoryRequestIsResolved(int categoryRequestId) {
        return categoryRequestService.isResolved(categoryRequestId);
    }

    @Operation(summary = "Accept or decline category request",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Category request status changed to resolved<br>" +
                            "If accepted, system creates new category, sets category hidden status to false or does nothing"),
                    @ApiResponse(responseCode = "404", description = "Category request with given ID does not exist in database"),
                    @ApiResponse(responseCode = "406", description = "Category request with given ID is already resolved")
            })
    @PreAuthorize("hasAuthority(@securityConfiguration.getAdminRole())")
    @PatchMapping("/{id}")
    public ResponseEntity<?> acceptCategoryRequest(@PathVariable("id") int categoryRequestId, @RequestParam("accept") boolean accept, @RequestParam(required = false, name = "message") String message) {
        if (accept)
            return categoryRequestService.setCategoryRequestAsAccepted(categoryRequestId, message);
        return categoryRequestService.setCategoryRequestAsDeclined(categoryRequestId, message);
    }
}
