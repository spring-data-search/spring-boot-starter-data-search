package app.commerceio.spring.data.search.jpa;


import org.junit.jupiter.api.Test;
import org.springframework.data.jpa.domain.Specification;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class SearchBuilderTest {

    @Test
    void parseBlankSearch() {
        String search = "";

        Specification<TestEntity> specification = new SearchBuilder().parse(search);
        assertNull(specification);
    }

    @Test
    void parseSingleCriteriaSearch() {
        String search = "firstName:Adam";

        Specification<TestEntity> specification = new SearchBuilder().parse(search);
        assertNotNull(specification);
    }
}