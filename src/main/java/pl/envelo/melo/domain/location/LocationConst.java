package pl.envelo.melo.domain.location;

public class LocationConst {
    public static final String MISSING_LOCATION_DATA= "Location fields (street name, number, postal code, city)" +
            " must be filled in, or all must be left blank";
    public static final String BLANK_STREET_NAME = "streetName field can't be blank";
    public static final String BLANK_STREET_NUMBER = "streetNumber field can't be blank";
    public static final String BLANK_POSTAL_CODE = "postalCode field can't be blank";
    public static final String BLANK_CITY = "city field can't be blank";
    public static final String INVALID_STREET_NAME_LENGTH = "the length of streetName must not be greater than 255";
    public static final String INVALID_STREET_NUMBER_LENGTH = "streetNumber is greater than world's highest street address number.";
    public static final String INVALID_APARTMENT_NUMBER_LENGTH = "apartmentNumber is to big. Really? SBM Validation Error.";
    public static final String INVALID_CITY_LENGTH = "the length of city must not be greater than 255";
    public static final String INVALID_STREET_NAME= "Invalid street name, " +
            "street name shouldn't have strange characters except '.', '-' and '/'";
    public static final String INVALID_STREET_NUMBER = "Invalid street number, " +
            "enter a valid number according to format X - XXXX/XXXX/XXXX or Xa-z - XXXX/XXXX/XXXXa-z";
    public static final String INVALID_APARTMENT_NUMBER = "Invalid apartment number, " +
            "enter a valid number according to format 1-99999 or 1a-z-9999a-z";
    public static final String INVALID_POST_NUMBER = "Invalid postal code, " +
            "enter a valid code according to format XX-XXX";
    public static final String INVALID_CITY = "Invalid city name, " +
            "city name should have only letters";
}
