package dk.via.sep4.cloud.data;

import dk.via.sep4.cloud.data.repository.MongoRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataConfiguration {
    private static DataRepository repository;

    @Bean
    public DataRepository repository() {
        if (repository == null) {
            repository = new MongoRepository();
        }
        return repository;
    }
}
