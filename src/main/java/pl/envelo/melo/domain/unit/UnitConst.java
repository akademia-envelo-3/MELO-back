package pl.envelo.melo.domain.unit;

public class UnitConst {

    public static final int MIN_NAME_CHAR_AMOUNT = 2;
    public static final int MAX_NAME_CHAR_AMOUNT = 255;
    public static final int MAX_DESCRIPTION_CHAR_AMOUNT = 4000;
    public static final String UNIT_NOT_AVAILABLE = "Unit you tried to add is not available";
    public static final String INVALID_UNIT_NAME = "wrong unit name size (must be between 2 and 255)";
    public static final String INVALID_UNIT_DESCRIPTION_LENGTH = "too long description (max 4000 characters)";
    public static final String UNIT_DOES_NOT_EXIST = "Unit does not exist";
    public static final String ONE_OF_THE_ENTITIES_NOT_FOUND = "Atleast one of the provided entity ids does not exist in the database";
    public static final String EMPLOYEE_ALREADY_IN_UNIT = "Employee already in unit";
    public static final String UNIT_ORGANIZER_LEAVE_ATTEMPT = "Unit organizer cant be remove from his event";
    public static final String EMPLOYEE_NOT_IN_MEMBER_LIST = "Employee is not member of this unit.";
    public static final String AMBIGUOUS_UNIT_NAME = "Unit with this name already exist";
    public static final String UNIT_NAME_NOT_CHANGED = "Unit name in database is the same you're trying to send.";
    public static final String UNIT_DESCRIPTION_NOT_CHANGED = "Unit name in database is the same you're trying to send.";


}
