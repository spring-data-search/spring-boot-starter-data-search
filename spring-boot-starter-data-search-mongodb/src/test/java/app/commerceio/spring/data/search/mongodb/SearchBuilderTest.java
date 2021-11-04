package app.commerceio.spring.data.search.mongodb;

import com.mongodb.BasicDBList;
import app.commerceio.spring.data.search.mongodb.SearchBuilder;
import org.bson.Document;
import org.junit.jupiter.api.Test;
import org.springframework.data.mongodb.core.query.Criteria;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class SearchBuilderTest {

    @Test
    void parseBlankSearch() {
        String search = "";

        Criteria criteria = new SearchBuilder().parse(search);
        assertNull(criteria);
    }

    @Test
    void parseSingleCriteriaSearch() {
        String search = "name:myName";

        Criteria criteria = new SearchBuilder().parse(search);
        assertNotNull(criteria);
    }

    @Test
    void parseAllCriteriaSearch() {
        String search = "(name:myName AND age>:33) OR (name:myName2,myName3 AND age:17,22,43) OR (name:/.*m.*/gimscxdtu AND age<20)";

        Criteria criteria = new SearchBuilder().parse(search);
        assertNotNull(criteria);
    }

    @Test
    void andTwoCriteria() {
        String searchByName = "name:myName";
        String searchByAge = "age>:33";

        SearchBuilder searchBuilder = new SearchBuilder();
        Criteria nameCriteria = searchBuilder.parse(searchByName);
        assertNotNull(nameCriteria);

        Criteria ageCriteria = searchBuilder.parse(searchByAge);
        assertNotNull(ageCriteria);

        Criteria criteria = searchBuilder.and(nameCriteria, ageCriteria);
        Document criteriaObject = criteria.getCriteriaObject();
        assertNotNull(criteriaObject);

        BasicDBList basicDBList = criteriaObject.get("$and", BasicDBList.class);
        assertEquals(2, basicDBList.size());
    }

    @Test
    void orTwoCriteria() {
        String searchByName = "name:myName";
        String searchByAge = "age>:33";

        SearchBuilder searchBuilder = new SearchBuilder();
        Criteria nameCriteria = searchBuilder.parse(searchByName);
        assertNotNull(nameCriteria);

        Criteria ageCriteria = searchBuilder.parse(searchByAge);
        assertNotNull(ageCriteria);

        Criteria criteria = searchBuilder.or(nameCriteria, ageCriteria);
        Document criteriaObject = criteria.getCriteriaObject();
        assertNotNull(criteriaObject);

        BasicDBList basicDBList = criteriaObject.get("$or", BasicDBList.class);
        assertEquals(2, basicDBList.size());
    }

    @Test
    void andNullCriteria() {
        SearchBuilder searchBuilder = new SearchBuilder();
        Criteria[] nullCriteria = null;
        Criteria criteria = searchBuilder.and(nullCriteria);
        assertNull(criteria);
    }

    @Test
    void orNullCriteria() {
        SearchBuilder searchBuilder = new SearchBuilder();
        Criteria[] nullCriteria = null;
        Criteria criteria = searchBuilder.or(nullCriteria);
        assertNull(criteria);
    }
}