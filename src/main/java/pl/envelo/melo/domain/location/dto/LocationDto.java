package pl.envelo.melo.domain.location.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.MatchResult;
import lombok.Setter;
import pl.envelo.melo.exceptions.LocationBadRequestException;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LocationDto {

    @NotEmpty(message = "streetName field can't be blank")
    @Max(value = 255, message = "the length of streetName must not be greater than 255")
    private String streetName;

    @NotEmpty(message = "streetNumber field can't be blank")
    @Max(value = 100, message = "streetNumber is greater than world's highest street address number.")
    private String streetNumber;

//    @NotBlank(message = "apartmentNumber field can't be blank")
    @Max(value = 100, message = "apartmentNumber is to big. Really? SBM Validation Error.")
    private String apartmentNumber;

    @NotEmpty(message = "postalCode field can't be blank")
    @Size(min = 6, max = 6)
    private String postalCode;

    @NotEmpty(message = "city field can't be blank")
    @Max(value = 255, message = "the length of city must not be greater than 255")
    private String city;

    public void setStreetName(String streetName) {
        char[] charStreet = streetName.toCharArray();

        for (char c : charStreet) {
            if (c >= (int) 65 && c <= (int) 90
                    || c >= (int) 97 && c <= (int) 122
                    || c >= (int) 260 && c <= (int) 380
                    || c >= (int) 45 && c <= (int) 57
                    || c ==' '){
                this.streetName = streetName;
            }
            else
                throw new LocationBadRequestException("Invalid street name, " +
                        "street name shouldn't have strange characters except '.', '-' and '/'");
        }
    }

    public void setStreetNumber(String streetNumber) {
        String regexOnlyNumbers = "^\\d+(?:[/\\S]\\d+)?(?:[/\\S]\\d+)?";
        String regexNumbersWithLetter = "^\\d+(?:[/\\S]\\d+)?(?:[/\\S]\\d+)?[A-Za-z]";
        Pattern patternNum = Pattern.compile(regexOnlyNumbers);
        Pattern patternLet = Pattern.compile(regexNumbersWithLetter);
        if(patternNum.matcher(streetNumber).matches()
                || patternLet.matcher(streetNumber).matches()
                || streetNumber.equals("")) {
            this.streetNumber = streetNumber;
        } else
            throw new LocationBadRequestException("Invalid street number, " +
                    "enter a valid number according to format X - XXXX/XXXX/XXXX or Xa-z - XXXX/XXXX/XXXXa-z");
    }

    public void setApartmentNumber(String apartmentNumber) {
        String regexOnlyNumbers = "^\\d+";
        String regexNumbersWithLetter = "^\\d+[A-Za-z]";
        Pattern patternNum = Pattern.compile(regexOnlyNumbers);
        Pattern patternLet = Pattern.compile(regexNumbersWithLetter);
        if(patternNum.matcher(apartmentNumber).matches() && apartmentNumber.length() <= 5
                || patternLet.matcher(apartmentNumber).matches()&& apartmentNumber.length() <= 5
                || apartmentNumber.equals("") ) {
            this.apartmentNumber = apartmentNumber;
        } else
            throw new LocationBadRequestException("Invalid apartment number, " +
                    "enter a valid number according to format 1-99999 or 1a-z-9999a-z");
    }

    public void setPostalCode(String postalCode) {
        String regex = "^[0-9]{2}-[0-9]{3}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(postalCode);
        if (matcher.matches() || postalCode.equals("")){
            this.postalCode = postalCode;
        } else
            throw new LocationBadRequestException("Invalid postal code, " +
                    "enter a valid code according to format XX-XXX");
    }

    public void setCity(String city) {
        char[] charCity = city.toCharArray();

        for (char c : charCity) {
            if (c >= (int) 65 && c <= (int) 90
                    || c >= (int) 97 && c <= (int) 122
                    || c >= (int) 260 && c <= (int) 380
                    || c == (int) 45){
                this.city = city;
            }
            else
                throw new LocationBadRequestException("Invalid city name, " +
                        "city name should have only letters");
        }

    }
}
