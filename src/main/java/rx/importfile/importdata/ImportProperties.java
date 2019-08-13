package rx.importfile.importdata;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "import")
public class ImportProperties {
    private String filePath;
    private String table;
    private Integer batchSize;
}
