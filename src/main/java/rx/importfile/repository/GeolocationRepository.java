package rx.importfile.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import rx.importfile.model.Geolocation;

@Component
@RequiredArgsConstructor
public class GeolocationRepository {

    private final JdbcTemplate jdbcTemplate;


    public Geolocation findByIpAddress(String ipaddress) {
        return jdbcTemplate.queryForObject("SELECT country_code, country, city, latitude,longitude,mystery_value" +
                        " FROM IPADDRESS  WHERE ip_address=?", new Object[]{ipaddress},
                (rs, rowNum) -> new Geolocation(rs.getString("country_code"),
                        rs.getString("country"),
                        rs.getString("city"),
                        rs.getString("latitude"),
                        rs.getString("longitude"),
                        rs.getString("mystery_value")));
    }
}
