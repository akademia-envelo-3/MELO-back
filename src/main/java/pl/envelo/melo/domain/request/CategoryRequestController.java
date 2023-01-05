package pl.envelo.melo.domain.request;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import pl.envelo.melo.domain.request.dto.CategoryRequestDto;

import java.util.List;

@RestController
@AllArgsConstructor
public class CategoryRequestController {

    private CategoryRequestService categoryRequestService;
    private NotificationService notificationService;
    private CategoryService categoryService;

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
