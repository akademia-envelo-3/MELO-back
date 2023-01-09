package pl.envelo.melo.domain.request;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import pl.envelo.melo.domain.request.dto.CategoryRequestDto;

import java.util.List;

@RestController
@AllArgsConstructor
public class CategoryRequestController {

    private final CategoryRequestService categoryRequestService;
    private final NotificationService notificationService;
    private final CategoryService categoryService;

    public CategoryRequest addNewCategoryRequest(CategoryRequestDto categoryRequestDto) {
        return categoryRequestService.insertNewCategoryRequest(categoryRequestDto);
    }

    public List<CategoryRequest> showAllCategoryRequests() {
        return categoryRequestService.listAllCategoryRequest();
    }

    public boolean checkIfCategoryRequestIsResolved(int categoryRequestId) {
        return categoryRequestService.isResolved(categoryRequestId);
    }

    public boolean acceptCategoryRequest(int categoryRequestId) {
        return categoryRequestService.setCategoryRequestAsAccepted(categoryRequestId);
    }

    public boolean declineCategoryRequest(int categoryRequestId) {
        return categoryRequestService.setCategoryRequestAsDeclined(categoryRequestId);
    }

}
