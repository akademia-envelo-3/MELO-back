package pl.envelo.melo.exceptions;

public class CategoryRequestAlreadyResolvedException extends RuntimeException{
    public CategoryRequestAlreadyResolvedException() {
        super("Category request already resolved");
    }
}
