package pl.envelo.melo.exceptions;

public class CategoryAlreadyExistsException extends BadRequestException{
    public CategoryAlreadyExistsException() {
        super("Category already exists");
    }
}

