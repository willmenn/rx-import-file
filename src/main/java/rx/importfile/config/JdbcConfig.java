package rx.importfile.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@Slf4j
public class JdbcConfig {

    @Bean
    public DataSource dataSource(DatabaseProperties props) {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName(props.getDriver());
        dataSource.setUrl(props.getUrl());
        dataSource.setUsername(props.getUser());
        dataSource.setPassword(props.getPass());
        log.info("driver: {} , url-db: {}, user: {}, pass: {}",
                props.getDriver(),
                props.getUrl(),
                props.getUser(),
                props.getPass());
        return dataSource;
    }

    @Data
    @Configuration
    @ConfigurationProperties(prefix = "application.jdbc")
    public static class DatabaseProperties {
        private String driver;
        private String url;
        private String user;
        private String pass;
    }
}
