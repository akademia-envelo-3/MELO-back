package pl.envelo.melo.domain.request;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.envelo.melo.domain.request.dto.CategoryRequestDto;

import java.util.List;

@Service
@AllArgsConstructor
public class CategoryRequestService {

    private final CategoryRequestRepository categoryRequestRepository;

    public CategoryRequest insertNewCategoryRequest(CategoryRequestDto categoryRequestDto) {
        return null;
    }

    public List<CategoryRequest> listAllCategoryRequest() {
        return null;
    }

    public boolean isResolved(int categoryRequestId) {
        return false;
    }

    public boolean setCategoryRequestAsAccepted(int categoryRequestId) {
        return false;
    }

    public boolean setCategoryRequestAsDeclined(int categoryRequestId) {
        return false;
    }


}
