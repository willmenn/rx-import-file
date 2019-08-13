package rx.importfile.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rx.importfile.model.Geolocation;
import rx.importfile.repository.GeolocationRepository;

@RestController
@RequestMapping("/geolocation")
@RequiredArgsConstructor
public class GeolocationController {

    private final GeolocationRepository repository;

    @GetMapping("/{ipaddress}")
    public Geolocation getGeolocation(@PathVariable("ipaddress") String ipaddress) {
        return repository.findByIpAddress(ipaddress);
    }
}
