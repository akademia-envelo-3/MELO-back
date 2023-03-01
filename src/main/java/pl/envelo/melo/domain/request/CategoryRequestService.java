package pl.envelo.melo.domain.request;

import jakarta.transaction.Transactional;
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
import pl.envelo.melo.exceptions.CategoryNotFoundException;
import pl.envelo.melo.exceptions.CategoryRequestAlreadyExistsException;
import pl.envelo.melo.exceptions.CategoryRequestAlreadyResolvedException;
import pl.envelo.melo.mappers.CategoryRequestMapper;

import java.security.Principal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 This class handles the CRUD operations for CategoryRequest objects and
 serves as a bridge between the application and the database.
 It also provides additional functionality related to category requests.
 */
@Service
@AllArgsConstructor
@Transactional
public class CategoryRequestService {

    private final CategoryRequestRepository categoryRequestRepository;
    private final CategoryRepository categoryRepository;
    private final CategoryService categoryService;
    private final EmployeeRepository employeeRepository;
    private final NotificationService notificationService;
    private final AuthorizationService authorizationService;

    private final CategoryRequestMapper categoryRequestMapper;

    /**
     Inserts a new category request into the system.
     @param categoryDto A DTO containing information about the category being requested.
     @param principal The authenticated user making the request.
     @return A ResponseEntity containing a DTO representation of the newly created category request.
     @throws CategoryAlreadyExistsException if the category already exists in the system and is not hidden.
     @throws CategoryRequestAlreadyExistsException if a category request with the same name already exists and is unresolved.
     */
    public ResponseEntity<?> insertNewCategoryRequest(CategoryDto categoryDto, Principal principal) {
        categoryDto.setName(categoryDto.getName().toLowerCase().replaceAll("\\s+", " ").trim());
        Category category = categoryRepository.findByName(categoryDto.getName());
        if (category != null && category.getName().equals(categoryDto.getName()) && !category.isHidden())
            throw new CategoryAlreadyExistsException();
        CategoryRequest categoryRequestInDatabase = categoryRequestRepository.findByCategoryName(categoryDto.getName());
        if (categoryRequestInDatabase != null
                && categoryRequestInDatabase.getCategoryName().equals(categoryDto.getName())
                && !categoryRequestInDatabase.isResolved())
            throw new CategoryRequestAlreadyExistsException();

        CategoryRequest categoryRequest = new CategoryRequest();
        authorizationService.createUser(principal);
        Employee employee = employeeRepository.findByUserId(authorizationService.getUUID(principal)).get();
        categoryRequest.setEmployee(employee);
        categoryRequest.setCategoryName(categoryDto.getName().toLowerCase().trim());
        return ResponseEntity.ok(categoryRequestMapper.toDto(categoryRequestRepository.save(categoryRequest)));
    }

    /**
     * Retrieves a list of CategoryRequestToDisplayOnListDto objects from the database based on whether they have been resolved or not.
     * @param resolved - a boolean indicating whether to return resolved (true) or unresolved (false) category requests
     * @return a ResponseEntity containing a List of CategoryRequestToDisplayOnListDto objects
     */
    public ResponseEntity<List<CategoryRequestToDisplayOnListDto>> listCategoryRequests(boolean resolved) {
        return ResponseEntity.ok(categoryRequestRepository.findAllByIsResolved(resolved).stream().map(categoryRequestMapper::toDisplayOnListDto).collect(Collectors.toList()));
    }

    public ResponseEntity<?> isResolved(int categoryRequestId) {
        return null;
    }

    /**
     * Sets a CategoryRequest as resolved, creates a Category and sends a notification based on whether the Category already exists or needs to be created.
     * @param categoryRequestId - the ID of the CategoryRequest to set as resolved
     * @param message - an optional message to include in the notification
     * @return a ResponseEntity indicating the success of the operation
     */
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

    /**
     This method sets a category request as declined, sends a notification to the employee who requested the category,
     and returns an HTTP response with status 200 OK.
     @param categoryRequestId an integer representing the ID of the category request to be set as declined
     @param message a string representing the message to be included in the notification sent to the employee who requested the category, can be null
     @return ResponseEntity with status 200 OK
     @throws CategoryNotFoundException if the category request with the given ID does not exist
     @throws CategoryRequestAlreadyResolvedException if the category request with the given ID is already resolved
     */
     public ResponseEntity<?> setCategoryRequestAsDeclined(int categoryRequestId, String message) {
            sendCategoryRequestNotification(setCategoryRequestAsResolved(categoryRequestId), message, NotificationType.CATEGORY_REQUEST_REJECTED);
            return ResponseEntity.ok().build();
        }

    /**
     This method sets a category request with the given ID as resolved and returns the resolved category request.
     @param categoryRequestId an integer representing the ID of the category request to be set as resolved
     @return the resolved CategoryRequest object
     @throws CategoryNotFoundException if the category request with the given ID does not exist
     @throws CategoryRequestAlreadyResolvedException if the category request with the given ID is already resolved
     */
     private CategoryRequest setCategoryRequestAsResolved(int categoryRequestId) {
            CategoryRequest categoryRequest = categoryRequestRepository.findById(categoryRequestId).orElseThrow(() -> {
                throw new CategoryNotFoundException();
            });
            if (categoryRequest.isResolved())
                throw new CategoryRequestAlreadyResolvedException();
            categoryRequest.setResolved(true);
            categoryRequestRepository.save(categoryRequest);
            return categoryRequest;
        }

    /**
     This method sends a notification to the employee who requested the category with the given category request,
     containing a message and a notification type.
     @param categoryRequest a CategoryRequest object representing the category request for which the notification is being sent
     @param message a string representing the message to be included in the notification sent to the employee who requested the category, can be null
     @param notificationType a NotificationType enum representing the type of notification to be sent
     */
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
