package pl.envelo.melo.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ApplicationExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadRequestException.class)
    public Map<String, String> handleInvalidLocationArgument(BadRequestException ex) {
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("error : ", ex.getMessage());
        return errorMap;
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ResourceNotFoundException.class)
    public Map<String, String> handleResourceNotFound(ResourceNotFoundException ex) {
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("Resource not found", ex.getMessage());
        return errorMap;
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(AppUserNotFoundException.class)
    public Map<String, String> handleAppUserNotFound(AppUserNotFoundException ex) {
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("error : ", ex.getMessage());
        return errorMap;
    }

    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    @ExceptionHandler(CategoryRequestAlreadyResolvedException.class)
    public Map<String, String> handleCategoryRequestAlreadyResolved(CategoryRequestAlreadyResolvedException ex) {
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("error : ", ex.getMessage());
        return errorMap;
    }
}
