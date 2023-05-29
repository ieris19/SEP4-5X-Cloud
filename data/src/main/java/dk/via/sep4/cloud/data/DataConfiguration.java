package dk.via.sep4.cloud.data;

import dk.via.sep4.cloud.data.repository.MongoRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_SINGLETON;

/**
 * This class is used to create Beans for the spring context.
 */
@Configuration
public class DataConfiguration {
    @Bean
    @Scope(scopeName = SCOPE_SINGLETON)
    public MongoRepository repository() {
        return new MongoRepository();
    }
}
