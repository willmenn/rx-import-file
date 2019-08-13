package rx.importfile.controller;

import com.findhotel.ipaddress.model.Geolocation;
import com.findhotel.ipaddress.repository.IpAddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/geolocation")
@RequiredArgsConstructor
public class GeolocationController {

    private final IpAddressRepository repository;

    @GetMapping("/{ipaddress}")
    public Geolocation getGeolocation(@PathVariable("ipaddress") String ipaddress) {
        return repository.findByIpAddress(ipaddress);
    }
}
