package pl.envelo.melo.domain.location.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.envelo.melo.exceptions.LocationBadRequestException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

        if (streetName.equals("")) {
            this.streetName = streetName;
        } else {
            for (char c : charStreet) {
                if (c >= (int) 65 && c <= (int) 90
                        || c >= (int) 97 && c <= (int) 122
                        || c >= (int) 260 && c <= (int) 380
                        || c >= (int) 45 && c <= (int) 57
                        || c == (int) 211 || c == (int) 243
                        || c == ' ') {
                    this.streetName = streetName.replaceAll("( +)", " ").trim();
                } else
                    throw new LocationBadRequestException("Invalid street name, " +
                            "street name shouldn't have strange characters except '.', '-' and '/'");
            }
        }
    }

    public void setStreetNumber(String streetNumber) {
        String regexOnlyNumbers = "^\\d+(?:[/\\S]\\d+)?(?:[/\\S]\\d+)?";
        String regexNumbersWithLetter = "^\\d+(?:[/\\S]\\d+)?(?:[/\\S]\\d+)?[A-Za-z]";
        Pattern patternNum = Pattern.compile(regexOnlyNumbers);
        Pattern patternLet = Pattern.compile(regexNumbersWithLetter);
        if (patternNum.matcher(streetNumber.trim()).matches()
                || patternLet.matcher(streetNumber.trim()).matches()
                || streetNumber.trim().equals("")) {
            this.streetNumber = streetNumber.trim();
        } else
            throw new LocationBadRequestException("Invalid street number, " +
                    "enter a valid number according to format X - XXXX/XXXX/XXXX or Xa-z - XXXX/XXXX/XXXXa-z");
    }

    public void setApartmentNumber(String apartmentNumber) {
        String regexOnlyNumbers = "^\\d+";
        String regexNumbersWithLetter = "^\\d+[A-Za-z]";
        Pattern patternNum = Pattern.compile(regexOnlyNumbers);
        Pattern patternLet = Pattern.compile(regexNumbersWithLetter);
        if (patternNum.matcher(apartmentNumber.trim()).matches() && apartmentNumber.length() <= 5
                || patternLet.matcher(apartmentNumber.trim()).matches() && apartmentNumber.length() <= 5
                || apartmentNumber.trim().equals("")) {
            this.apartmentNumber = apartmentNumber.trim();
        } else
            throw new LocationBadRequestException("Invalid apartment number, " +
                    "enter a valid number according to format 1-99999 or 1a-z-9999a-z");
    }

    public void setPostalCode(String postalCode) {
        String regex = "^[0-9]{2}-[0-9]{3}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(postalCode.trim());
        if (matcher.matches() || postalCode.trim().equals("")) {
            this.postalCode = postalCode.trim();
        } else
            throw new LocationBadRequestException("Invalid postal code, " +
                    "enter a valid code according to format XX-XXX");
    }

    public void setCity(String city) {
        char[] charCity = city.toCharArray();

        if (city.trim().equals("")) {
            this.city = city.trim();
        } else {
            for (char c : charCity) {
                if (c >= (int) 65 && c <= (int) 90
                        || c >= (int) 97 && c <= (int) 122
                        || c >= (int) 260 && c <= (int) 380
                        || c == (int) 211 || c == (int) 243
                        || c == (int) 45 || c == ' ') {
                    this.city = city.replaceAll("( +)", " ").trim();
                } else
                    throw new LocationBadRequestException("Invalid city name, " +
                            "city name should have only letters");
            }

        }
    }
}
