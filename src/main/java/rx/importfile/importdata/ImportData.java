package rx.importfile.importdata;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.StopWatch;
import rx.Observable;
import rx.schedulers.Schedulers;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.lang.String.format;
import static java.nio.file.Files.lines;
import static java.nio.file.Paths.get;
import static rx.Observable.from;


@Configuration
@RequiredArgsConstructor
@Slf4j
@Profile("load-data")
public class ImportData {

    private static final int FILE_LINES_COUNT = 999999;
    private static final String COMMA = ",";
    private static final String INSERT_FORMAT = "INSERT INTO %s " +
            "(ip_address,country_code,country,city,latitude,longitude,mystery_value)" +
            "VALUES(\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\")";
    private final JdbcTemplate jdbcTemplate;
    private final ImportProperties props;

    @Bean
    public int loadDataProcessor(@Qualifier("statistics-map")
                                         ConcurrentHashMap<String, Integer> statisticsMap) {
        log.info("Starting Loading Data, with properties {}", props.toString());
        try {
            StopWatch stopWatch = new StopWatch();
            stopWatch.start();

            String path = new ClassPathResource(props.getFilePath()).getPath();
            log.info(path);
            Set<String> inserts = lines(get(props.getFilePath()))
                    .skip(1)
                    .map(line -> line.split(COMMA))
                    .map(GeolocationImportModel::from)
                    .filter(GeolocationImportModel::isValid)
                    .map(this::createInsert)
                    .collect(Collectors.toSet());

            from(inserts)
                    .buffer(props.getBatchSize())
                    .flatMap(this::executeBatchInParallel)
                    .onErrorResumeNext(this::onErrorEmitEmptyObservableAndLog)
                    .doOnCompleted(() -> buildStatisticsAndLog(statisticsMap, stopWatch, inserts))
                    .subscribe(this::logResults);

        } catch (IOException e) {
            String msg = "Could not find the file.";
            log.error(msg, e);
            throw new RuntimeException(msg, e);
        }
        return 0;
    }

    private Observable<Integer> executeBatchInParallel(List<String> list) {
        return Observable.just(list)
                .subscribeOn(Schedulers.io())
                .map(this::executeBatch);
    }

    private void buildStatisticsAndLog(@Qualifier("statistics-map") ConcurrentHashMap<String, Integer> statisticsMap,
                                       StopWatch stopWatch, Set<String> inserts) {
        stopWatch.stop();
        int missedLines = FILE_LINES_COUNT - inserts.size();
        double totalTimeSeconds = stopWatch.getTotalTimeSeconds();
        log.info("Total {} , inserted: {}, total time elapsed: {}",
                missedLines, inserts.size(), totalTimeSeconds);
        statisticsMap.put("totalLinesInserted", inserts.size());
        statisticsMap.put("missedLines", missedLines);
        statisticsMap.put("totalTimeInSec", (int) totalTimeSeconds);
    }

    private void logResults(Integer results) {
        log.info("Results sum for each batch: {}", results);
    }

    private Observable<? extends Integer> onErrorEmitEmptyObservableAndLog(Throwable error) {
        log.info(error.getMessage());
        return Observable.empty();
    }

    private int executeBatch(List<String> inserts) {
        int[] results = jdbcTemplate.batchUpdate(inserts.toArray(new String[props.getBatchSize()]));
        return IntStream.of(results).sum();
    }

    private String createInsert(GeolocationImportModel item) {
        return format(INSERT_FORMAT,
                props.getTable(),
                clearFromTwoQuotes(item.getIpAddress()),
                clearFromTwoQuotes(item.getCountry_code()),
                clearFromTwoQuotes(item.getCountry()),
                clearFromTwoQuotes(item.getCity()),
                clearFromTwoQuotes(item.getLatitude()),
                clearFromTwoQuotes(item.getLongitude()),
                clearFromTwoQuotes(item.getMystery_value()));
    }

    private String clearFromTwoQuotes(String value) {
        return value.replace("\"", "");
    }
}
