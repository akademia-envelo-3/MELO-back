package pl.envelo.melo.domain.request;

import lombok.AllArgsConstructor;
import org.aspectj.weaver.ast.Not;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import pl.envelo.melo.authorization.employee.EmployeeRepository;
import pl.envelo.melo.domain.category.Category;
import pl.envelo.melo.domain.category.CategoryDto;
import pl.envelo.melo.domain.category.CategoryRepository;
import pl.envelo.melo.domain.category.CategoryService;
import pl.envelo.melo.domain.notification.NotificationService;
import pl.envelo.melo.domain.notification.NotificationType;
import pl.envelo.melo.domain.notification.dto.RequestNotificationDto;
import pl.envelo.melo.domain.request.dto.CategoryRequestDto;
import pl.envelo.melo.exceptions.CategoryRequestAlreadyResolvedException;
import pl.envelo.melo.exceptions.ResourceNotFoundException;

import java.util.List;
import java.util.Objects;
import pl.envelo.melo.domain.request.dto.CategoryRequestToDisplayOnListDto;
import pl.envelo.melo.mappers.CategoryRequestMapper;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CategoryRequestService {

    private final CategoryRequestRepository categoryRequestRepository;
    private final CategoryRepository categoryRepository;
    private final CategoryService categoryService;
    private final EmployeeRepository employeeRepository;
    private final NotificationService notificationService;

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

    public ResponseEntity<?> setCategoryRequestAsAccepted(int categoryRequestId, String message) {
        CategoryRequest categoryRequest = setCategoryRequestAsResolved(categoryRequestId);
        Category category = categoryRepository.findByName(categoryRequest.getCategoryName());
        if (Objects.isNull(category) || category.isHidden())
            sendCategoryRequestNotification(categoryRequest, message, NotificationType.CATEGORY_REQUEST_ACCEPTED);
        if (Objects.isNull(category))
            return categoryService.insertNewCategory(new CategoryDto(categoryRequest.getCategoryName()));
        if (category.isHidden())
            return categoryService.changeStatusCategory(category.getId());
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<?> setCategoryRequestAsDeclined(int categoryRequestId, String message) {
        sendCategoryRequestNotification(setCategoryRequestAsResolved(categoryRequestId), message, NotificationType.CATEGORY_REQUEST_REJECTED);
        return ResponseEntity.ok().build();
    }

    private CategoryRequest setCategoryRequestAsResolved(int categoryRequestId) {
        CategoryRequest categoryRequest = categoryRequestRepository.findById(categoryRequestId).orElseThrow(() -> {
            throw new ResourceNotFoundException("Category request not found");
        });
        if (categoryRequest.isResolved())
            throw new CategoryRequestAlreadyResolvedException();
        categoryRequest.setResolved(true);
        categoryRequestRepository.save(categoryRequest);
        return categoryRequest;
    }

    private void sendCategoryRequestNotification(CategoryRequest categoryRequest, String message, NotificationType notificationType) {
        RequestNotificationDto requestNotificationDto = new RequestNotificationDto();
        requestNotificationDto.setEmployeeId(categoryRequest.getEmployee().getId());
        if(notificationType.equals(NotificationType.CATEGORY_REQUEST_ACCEPTED)) {
            requestNotificationDto.setReason("Twoja propozycja kategorii \""
                    +categoryRequest.getCategoryName()+"\" została zatwierdzona.");
        }
        if(notificationType.equals(NotificationType.CATEGORY_REQUEST_REJECTED)) {
            requestNotificationDto.setReason("Twoja propozycja kategorii \""
                    +categoryRequest.getCategoryName()+"\" została odrzucona.");
        }
        if(!message.isEmpty())
            requestNotificationDto.setReason(requestNotificationDto.getReason()+" Komentarz: "+message);
        requestNotificationDto.setNotificationType(notificationType);
        notificationService.insertRequestNotification(requestNotificationDto);
    }

}
