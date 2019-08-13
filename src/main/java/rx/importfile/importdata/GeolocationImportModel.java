package rx.importfile.importdata;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;
import java.util.regex.Pattern;

@AllArgsConstructor
@Getter
public class GeolocationImportModel {
    private static final Pattern PATTERN_IPV4 = Pattern.compile(
            "^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");
    private static final Pattern PATTERN_LATITUDE = Pattern.compile(
            "^[-+]?([1-8]?\\d(\\.\\d+)?|90(\\.0+)?)$");
    private static final Pattern PATTERN_LONGITUDE = Pattern.compile(
            "^\\s*[-+]?(180(\\.0+)?|((1[0-7]\\d)|([1-9]?\\d))(\\.\\d+)?)$");
    private static final Pattern PATTERN_CITY = Pattern.compile(
            "^[a-zA-Z\\s]*$");

    private String ipAddress;
    private String country_code;
    private String country;
    private String city;
    private String latitude;
    private String longitude;
    private String mystery_value;

    boolean isValid() {
        return validateIpAddress(this.ipAddress)
                && validateCountry(this.country)
                && validateCountryCode(this.country_code)
                && validateCity(this.city)
                && validateLatitude(this.latitude)
                && validateLongitude(this.longitude)
                && validateMysteryValue(this.mystery_value);

    }

    static GeolocationImportModel from(String[] line) {
        return new GeolocationImportModel(line[0], line[1], line[2], line[3], line[4], line[5],
                line[6]);

    }

    private static boolean validateIpAddress(String ipAddress) {

        return !ipAddress.isEmpty() && PATTERN_IPV4.matcher(ipAddress).matches();
    }

    private static boolean validateCountryCode(String country_code) {
        return !country_code.isEmpty()
                && PATTERN_CITY.matcher(country_code).matches()
                && country_code.length() == 2;
    }

    private static boolean validateCountry(String country) {
        return !country.isEmpty() && PATTERN_CITY.matcher(country).matches();
    }

    private static boolean validateCity(String city) {
        return !city.isEmpty() && PATTERN_CITY.matcher(city).matches();
    }

    private static boolean validateLatitude(String latitude) {
        return !latitude.isEmpty() && PATTERN_LATITUDE.matcher(latitude).matches();
    }

    private static boolean validateLongitude(String longitude) {
        return !longitude.isEmpty() && PATTERN_LONGITUDE.matcher(longitude).matches();
    }

    private static boolean validateMysteryValue(String mystery_value) {
        return !mystery_value.isEmpty();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GeolocationImportModel geolocationImportModel1 = (GeolocationImportModel) o;
        return Objects.equals(ipAddress, geolocationImportModel1.ipAddress);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ipAddress);
    }
}