package io.commerce.mongo.search;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SearchConfiguration {

    @Bean
    public SearchCriteriaBuilder searchCriteriaBuilder() {
        return new SearchCriteriaBuilder();
    }
}
