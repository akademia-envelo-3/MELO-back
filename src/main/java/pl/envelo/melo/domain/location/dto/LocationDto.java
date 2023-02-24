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

import static pl.envelo.melo.domain.location.LocationConst.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LocationDto {

    @NotEmpty(message = BLANK_STREET_NAME)
    @Max(value = 255, message = INVALID_STREET_NAME_LENGTH)
    private String streetName;

    @NotEmpty(message = BLANK_STREET_NUMBER)
    @Max(value = 100, message = INVALID_STREET_NUMBER_LENGTH)
    private String streetNumber;

    @Max(value = 100, message = INVALID_APARTMENT_NUMBER_LENGTH)
    private String apartmentNumber;

    @NotEmpty(message = BLANK_POSTAL_CODE)
    @Size(min = 6, max = 6)
    private String postalCode;

    @NotEmpty(message = BLANK_CITY)
    @Max(value = 255, message = INVALID_CITY_LENGTH)
    private String city;

    public void setStreetName(String streetName) {
        char[] charStreet = streetName.toCharArray();

        if (streetName.equals("")) {
            this.streetName = streetName;
        } else {
            for (char c : charStreet) {
                if (c >= 65 && c <= 90
                        || c >= 97 && c <= 122
                        || c >= 260 && c <= 380
                        || c >= 45 && c <= 57
                        || c == 211 || c == 243
                        || c == ' ') {
                    this.streetName = streetName.replaceAll("( +)", " ").trim();
                } else
                    throw new LocationBadRequestException(INVALID_STREET_NAME);
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
            throw new LocationBadRequestException(INVALID_STREET_NUMBER);
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
            throw new LocationBadRequestException(INVALID_APARTMENT_NUMBER);
    }

    public void setPostalCode(String postalCode) {
        String regex = "^[0-9]{2}-[0-9]{3}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(postalCode.trim());
        if (matcher.matches() || postalCode.trim().equals("")) {
            this.postalCode = postalCode.trim();
        } else
            throw new LocationBadRequestException(INVALID_POST_NUMBER);
    }

    public void setCity(String city) {
        char[] charCity = city.toCharArray();

        if (city.trim().equals("")) {
            this.city = city.trim();
        } else {
            for (char c : charCity) {
                if (c >= 65 && c <= 90
                        || c >= 97 && c <= 122
                        || c >= 260 && c <= 380
                        || c == 211 || c == 243
                        || c == 45 || c == ' ') {
                    this.city = city.replaceAll("( +)", " ").trim();
                } else
                    throw new LocationBadRequestException(INVALID_CITY);
            }

        }
    }
}
