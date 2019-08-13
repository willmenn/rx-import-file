package rx.importfile.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Geolocation {
    private String countryCode;
    private String country;
    private String city;
    private String latitude;
    private String longitude;
    private String mysteryValue;
}
