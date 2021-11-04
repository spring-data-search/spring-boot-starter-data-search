package app.commerceio.spring.data.search;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SearchCriteriaTest {

    @Test
    void searchCriteriaString() {
        String key = "key";
        String value = "value";

        SearchCriteria searchCriteria = SearchCriteria.builder()
                .key(key)
                .op(SearchOp.EQ)
                .value(value)
                .exists(false)
                .build();

        assertNotNull(searchCriteria);
        assertEquals(String.class, searchCriteria.getType());
        assertFalse(searchCriteria.isArray());
    }

    @Test
    void searchCriteriaStringArray() {
        String key = "key";
        String value = "value,anotherValue";

        SearchCriteria searchCriteria = SearchCriteria.builder()
                .key(key)
                .op(SearchOp.EQ)
                .value(value)
                .exists(false)
                .build();

        assertNotNull(searchCriteria);
        assertEquals(String.class, searchCriteria.getType());
        assertTrue(searchCriteria.isArray());
    }

    @Test
    void searchCriteriaBoolean() {
        String key = "key";
        String value = "false";

        SearchCriteria searchCriteria = SearchCriteria.builder()
                .key(key)
                .op(SearchOp.EQ)
                .value(value)
                .exists(false)
                .build();

        assertNotNull(searchCriteria);
        assertEquals(Boolean.class, searchCriteria.getType());
        assertFalse(searchCriteria.isArray());
    }

    @Test
    void searchCriteriaBooleanArray() {
        String key = "key";
        String value = "false,true";

        SearchCriteria searchCriteria = SearchCriteria.builder()
                .key(key)
                .op(SearchOp.EQ)
                .value(value)
                .exists(false)
                .build();

        assertNotNull(searchCriteria);
        assertEquals(Boolean.class, searchCriteria.getType());
        assertTrue(searchCriteria.isArray());
    }

    @Test
    void searchCriteriaNumber() {
        String key = "key";
        String value = "10.9";

        SearchCriteria searchCriteria = SearchCriteria.builder()
                .key(key)
                .op(SearchOp.EQ)
                .value(value)
                .exists(false)
                .build();

        assertNotNull(searchCriteria);
        assertEquals(Number.class, searchCriteria.getType());
        assertFalse(searchCriteria.isArray());
    }

    @Test
    void searchCriteriaNumberArray() {
        String key = "key";
        String value = "10.9,658.123";

        SearchCriteria searchCriteria = SearchCriteria.builder()
                .key(key)
                .op(SearchOp.EQ)
                .value(value)
                .exists(false)
                .build();

        assertNotNull(searchCriteria);
        assertEquals(Number.class, searchCriteria.getType());
        assertTrue(searchCriteria.isArray());
    }

    @Test
    void searchCriteriaDate() {
        String key = "key";
        String value = "1984-11-27T05:36:32.00Z";

        SearchCriteria searchCriteria = SearchCriteria.builder()
                .key(key)
                .op(SearchOp.EQ)
                .value(value)
                .exists(false)
                .build();

        assertNotNull(searchCriteria);
        assertEquals(Instant.class, searchCriteria.getType());
        assertFalse(searchCriteria.isArray());
    }

    @Test
    void searchCriteriaDateArray() {
        String key = "key";
        String value = "1986-11-27T05:36:32.00Z,1984-11-27T05:36:32.00Z";

        SearchCriteria searchCriteria = SearchCriteria.builder()
                .key(key)
                .op(SearchOp.EQ)
                .value(value)
                .exists(false)
                .build();

        assertNotNull(searchCriteria);
        assertEquals(Instant.class, searchCriteria.getType());
        assertTrue(searchCriteria.isArray());
    }
}