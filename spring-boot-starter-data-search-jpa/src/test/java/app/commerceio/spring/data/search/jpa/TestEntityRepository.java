package app.commerceio.spring.data.search.jpa;

import org.springframework.stereotype.Repository;

@Repository
public interface TestEntityRepository extends SearchRepository<TestEntity, Long> {
}
