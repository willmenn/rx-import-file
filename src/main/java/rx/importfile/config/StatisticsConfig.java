package rx.importfile.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class StatisticsConfig {
    @Bean(name = "statistics-map")
    public ConcurrentHashMap<String, Integer> buildStatisticsMap() {
        return new ConcurrentHashMap<>();
    }
}
