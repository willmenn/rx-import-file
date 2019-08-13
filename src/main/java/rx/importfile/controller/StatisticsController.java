package rx.importfile.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/statistics")
@RequiredArgsConstructor
public class StatisticsController {

    @Qualifier("statistics-map")
    private final ConcurrentHashMap<String, Integer> statisticsMap;

    @GetMapping
    public ConcurrentHashMap<String, Integer> getStatisticsMap() {
        return statisticsMap;
    }
}
