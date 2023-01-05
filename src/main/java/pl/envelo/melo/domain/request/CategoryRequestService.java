package pl.envelo.melo.domain.request;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.envelo.melo.domain.request.dto.CategoryRequestDto;

import java.util.List;

@Service
@AllArgsConstructor
public class CategoryRequestService {

    private CategoryRequestRepository categoryRequestRepository;

    CategoryRequest insertNewCategoryRequest(CategoryRequestDto categoryRequestDto) {
        return null;
    }

    List<CategoryRequest> listAllCategoryRequest() {
        return null;
    }

    boolean isResolved(int categoryRequestId) {
        return false;
    }

    boolean setCategoryRequestAsAccepted(int categoryRequestId) {
        return false;
    }

    boolean setCategoryRequestAsDeclined(int categoryRequestId) {
        return false;
    }


}
