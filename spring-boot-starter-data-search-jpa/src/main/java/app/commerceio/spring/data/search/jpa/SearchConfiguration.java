package app.commerceio.spring.data.search.jpa;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SearchConfiguration {

    @Bean(name = "jpaSearchBuilder")
    public SearchBuilder jpaSearchBuilder() {
        return new SearchBuilder();
    }
}
