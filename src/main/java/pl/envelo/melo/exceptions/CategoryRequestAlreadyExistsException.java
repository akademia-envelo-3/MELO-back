package pl.envelo.melo.exceptions;

public class CategoryRequestAlreadyExistsException extends BadRequestException{
    public CategoryRequestAlreadyExistsException() {
        super("Category request with given name is already in queue.");
    }
}