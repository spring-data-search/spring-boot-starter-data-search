package app.commerceio.spring.data.search.mongodb;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SearchConfiguration {

    @Bean(name = "mongodbSearchBuilder")
    public SearchBuilder mongodbSearchBuilder() {
        return new SearchBuilder();
    }
}
