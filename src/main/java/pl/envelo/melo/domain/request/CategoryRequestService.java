package pl.envelo.melo.domain.request;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.envelo.melo.domain.request.dto.CategoryRequestDto;

import java.util.List;

@Service
@AllArgsConstructor
public class CategoryRequestService {

    private final CategoryRequestRepository categoryRequestRepository;

    public ResponseEntity<CategoryRequest> insertNewCategoryRequest(CategoryRequestDto categoryRequestDto) {
        return null;
    }

    public ResponseEntity<List<CategoryRequest>> listAllCategoryRequest() {
        return null;
    }

    public ResponseEntity<?> isResolved(int categoryRequestId) {
        return null;
    }

    public ResponseEntity<?> setCategoryRequestAsAccepted(int categoryRequestId) {
        return null;
    }

    public ResponseEntity<?> setCategoryRequestAsDeclined(int categoryRequestId) {
        return null;
    }


}
