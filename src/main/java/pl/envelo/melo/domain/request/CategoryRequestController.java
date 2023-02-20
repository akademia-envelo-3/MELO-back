package pl.envelo.melo.domain.request;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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

    public ResponseEntity<?> acceptCategoryRequest(int categoryRequestId) {
        return categoryRequestService.setCategoryRequestAsAccepted(categoryRequestId);
    }

    public ResponseEntity<?> declineCategoryRequest(int categoryRequestId) {
        return categoryRequestService.setCategoryRequestAsDeclined(categoryRequestId);
    }

}
