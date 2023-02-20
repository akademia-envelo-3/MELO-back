package pl.envelo.melo.domain.request;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import pl.envelo.melo.authorization.employee.dto.EmployeeNameDto;
import pl.envelo.melo.domain.request.dto.CategoryRequestDto;
import pl.envelo.melo.domain.request.dto.CategoryRequestToDisplayOnListDto;
import pl.envelo.melo.mappers.CategoryRequestMapper;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CategoryRequestService {

    private final CategoryRequestRepository categoryRequestRepository;
    private final CategoryRequestMapper categoryRequestMapper;

    public ResponseEntity<CategoryRequest> insertNewCategoryRequest(CategoryRequestDto categoryRequestDto) {
        return null;
    }

    public ResponseEntity<List<CategoryRequestToDisplayOnListDto>> listCategoryRequests(boolean resolved) {
        return ResponseEntity.ok(categoryRequestRepository.findAllByIsResolved(resolved).stream().map(categoryRequestMapper::toDisplayOnListDto).collect(Collectors.toList()));
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
