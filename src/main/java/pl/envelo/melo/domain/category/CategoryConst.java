package pl.envelo.melo.domain.category;

public class CategoryConst {
    public final static String INVALID_NAME = "Size of category name must be between 1 and 255.";
    public final static String CATEGORY_ALREADY_VISIBLE = "This category is already visible in database.";
    public final static String CATEGORY_ALREADY_EXISTS = "Category with given name already exists in database";
    public final static String CATEGORY_NOT_FOUND = "Category with given ID does not exist in database";
    public final static String CATEGORY_NOT_AVAILABLE_ANYMORE = "Category you tried to add is not available anymore";
    public final static int MIN_NAME_LENGTH = 1;
    public final static int MAX_NAME_LENGTH = 255;
}
