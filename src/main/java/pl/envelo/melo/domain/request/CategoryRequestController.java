package pl.envelo.melo.domain.request;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import pl.envelo.melo.domain.category.CategoryService;
import pl.envelo.melo.domain.notification.NotificationService;
import pl.envelo.melo.domain.request.dto.CategoryRequestDto;

import java.util.List;

@RestController
@AllArgsConstructor
public class CategoryRequestController {

    private final CategoryRequestService categoryRequestService;
    private final NotificationService notificationService;
    private final CategoryService categoryService;

    public ResponseEntity<CategoryRequest> addNewCategoryRequest(CategoryRequestDto categoryRequestDto) {
        return categoryRequestService.insertNewCategoryRequest(categoryRequestDto);
    }

    public ResponseEntity<List<CategoryRequest>> showAllCategoryRequests() {
        return categoryRequestService.listAllCategoryRequest();
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
