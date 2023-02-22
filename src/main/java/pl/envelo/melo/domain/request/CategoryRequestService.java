package pl.envelo.melo.domain.request;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.envelo.melo.authorization.AuthorizationService;
import pl.envelo.melo.authorization.employee.Employee;
import pl.envelo.melo.authorization.employee.EmployeeRepository;
import pl.envelo.melo.domain.category.Category;
import pl.envelo.melo.domain.category.CategoryDto;
import pl.envelo.melo.domain.category.CategoryRepository;
import pl.envelo.melo.domain.category.CategoryService;
import pl.envelo.melo.domain.notification.NotificationService;
import pl.envelo.melo.domain.notification.NotificationType;
import pl.envelo.melo.domain.notification.dto.RequestNotificationDto;
import pl.envelo.melo.domain.request.dto.CategoryRequestToDisplayOnListDto;
import pl.envelo.melo.exceptions.CategoryAlreadyExistsException;
import pl.envelo.melo.exceptions.CategoryRequestAlreadyExistsException;
import pl.envelo.melo.exceptions.CategoryRequestAlreadyResolvedException;
import pl.envelo.melo.exceptions.ResourceNotFoundException;
import pl.envelo.melo.mappers.CategoryRequestMapper;

import java.security.Principal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CategoryRequestService {

    private final CategoryRequestRepository categoryRequestRepository;
    private final CategoryRepository categoryRepository;
    private final CategoryService categoryService;
    private final EmployeeRepository employeeRepository;
    private final NotificationService notificationService;
    private final AuthorizationService authorizationService;

    private final CategoryRequestMapper categoryRequestMapper;

    public ResponseEntity<?> insertNewCategoryRequest(CategoryDto categoryDto, Principal principal) {
        categoryDto.setName(categoryDto.getName().toLowerCase().replaceAll("\\s+", " ").trim());
        Category category = categoryRepository.findByName(categoryDto.getName());
        if (category != null && category.getName().equals(categoryDto.getName()) && !category.isHidden())
            throw new CategoryAlreadyExistsException();
        CategoryRequest categoryRequestInDatabase = categoryRequestRepository.findByCategoryName(categoryDto.getName());
        if (categoryRequestInDatabase != null && categoryRequestInDatabase.getCategoryName().equals(categoryDto.getName()))
            throw new CategoryRequestAlreadyExistsException();

        CategoryRequest categoryRequest = new CategoryRequest();
        authorizationService.inflateUser(principal);
        Employee employee = employeeRepository.findByUserId(authorizationService.getUUID(principal)).get();
        categoryRequest.setEmployee(employee);
        categoryRequest.setCategoryName(categoryDto.getName().toLowerCase().trim());
        return ResponseEntity.ok(categoryRequestMapper.toDto(categoryRequestRepository.save(categoryRequest)));
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
            return categoryService.insertNewCategory(new CategoryDto(categoryRequest.getCategoryName().toLowerCase().replaceAll("\\s+", " ").trim()));
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
        if (notificationType.equals(NotificationType.CATEGORY_REQUEST_ACCEPTED)) {
            requestNotificationDto.setReason("Twoja propozycja kategorii \""
                    + categoryRequest.getCategoryName() + "\" została zatwierdzona.");
        }
        if (notificationType.equals(NotificationType.CATEGORY_REQUEST_REJECTED)) {
            requestNotificationDto.setReason("Twoja propozycja kategorii \""
                    + categoryRequest.getCategoryName() + "\" została odrzucona.");
        }
        if (message != null)
            requestNotificationDto.setReason(requestNotificationDto.getReason() + " Komentarz: \"" + message + "\"");
        requestNotificationDto.setNotificationType(notificationType);
        notificationService.insertRequestNotification(requestNotificationDto);
    }

}
