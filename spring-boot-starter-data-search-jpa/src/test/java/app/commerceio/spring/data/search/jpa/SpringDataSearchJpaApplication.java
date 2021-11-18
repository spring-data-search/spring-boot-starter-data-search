package app.commerceio.spring.data.search.jpa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(repositoryBaseClass = SearchRepositoryImpl.class)
public class SpringDataSearchJpaApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringDataSearchJpaApplication.class, args);
    }

}
