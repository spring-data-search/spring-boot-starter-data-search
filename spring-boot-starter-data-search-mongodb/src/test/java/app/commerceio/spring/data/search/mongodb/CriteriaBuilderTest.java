package app.commerceio.spring.data.search.mongodb;

import com.mongodb.BasicDBList;
import app.commerceio.spring.data.search.SearchCriteria;
import app.commerceio.spring.data.search.SearchOp;
import app.commerceio.spring.data.search.mongodb.CriteriaBuilder;
import org.bson.BsonRegularExpression;
import org.bson.Document;
import org.junit.jupiter.api.Test;
import org.springframework.data.mongodb.core.query.Criteria;

import java.time.Instant;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CriteriaBuilderTest {

    @Test
    void toCriteria_eqString() {
        String key = "name";
        String value = "myName";

        SearchCriteria searchCriteria = getSearchCriteria(key, value, SearchOp.EQ, false);
        Criteria criteria = CriteriaBuilder.builder()
                .searchCriteria(searchCriteria)
                .build()
                .toCriteria();

        assertNotNull(criteria);

        Document criteriaObject = criteria.getCriteriaObject();
        assertNotNull(criteriaObject);
        assertEquals(value, criteriaObject.getString(key));
    }

    @Test
    void toCriteria_eqNumber() {
        String key = "age";
        String value = "33.5";
        SearchCriteria searchCriteria = getSearchCriteria(key, value, SearchOp.EQ, false);
        Criteria criteria = CriteriaBuilder.builder()
                .searchCriteria(searchCriteria)
                .build()
                .toCriteria();

        assertNotNull(criteria);

        Document criteriaObject = criteria.getCriteriaObject();
        assertNotNull(criteriaObject);

        BasicDBList basicDBList = criteriaObject.get("$or", BasicDBList.class);
        assertEquals(2, basicDBList.size());

        Document stringDocument = (Document) basicDBList.get(0);
        Document numberDocument = (Document) basicDBList.get(1);
        assertEquals(value, stringDocument.getString(key));
        assertEquals(Double.valueOf(value), numberDocument.getDouble(key));
    }

    @Test
    void toCriteria_eqBoolean() {
        String key = "married";
        String value = "true";
        SearchCriteria searchCriteria = getSearchCriteria(key, value, SearchOp.EQ, false);
        Criteria criteria = CriteriaBuilder.builder()
                .searchCriteria(searchCriteria)
                .build()
                .toCriteria();

        assertNotNull(criteria);

        Document criteriaObject = criteria.getCriteriaObject();
        assertNotNull(criteriaObject);

        BasicDBList or = criteriaObject.get("$or", BasicDBList.class);
        assertNotNull(or);
        assertEquals(2, or.size());

        Document stringDocument = (Document) or.get(0);
        Document booleanDocument = (Document) or.get(1);
        assertEquals(value, stringDocument.getString(key));
        assertEquals(Boolean.valueOf(value), booleanDocument.getBoolean(key));
    }

    @Test
    void toCriteria_eqDate() {
        String key = "birthDate";
        String value = "1984-11-27T05:36:32.00Z";
        SearchCriteria searchCriteria = getSearchCriteria(key, value, SearchOp.EQ, false);
        Criteria criteria = CriteriaBuilder.builder()
                .searchCriteria(searchCriteria)
                .build()
                .toCriteria();

        assertNotNull(criteria);

        Document criteriaObject = criteria.getCriteriaObject();
        assertNotNull(criteriaObject);

        BasicDBList or = criteriaObject.get("$or", BasicDBList.class);
        assertNotNull(or);
        assertEquals(2, or.size());

        Document stringDocument = (Document) or.get(0);
        Document dateDocument = (Document) or.get(1);
        assertEquals(value, stringDocument.getString(key));
        assertEquals(getDateValue(value), dateDocument.get(key));
    }

    @Test
    void toCriteria_inString() {
        String key = "name";
        String value = "myName1,myName2,myNa\\,me3";

        SearchCriteria searchCriteria = getSearchCriteria(key, value, SearchOp.EQ, false);
        Criteria criteria = CriteriaBuilder.builder()
                .searchCriteria(searchCriteria)
                .build()
                .toCriteria();

        assertNotNull(criteria);

        Document document = (Document) criteria.getCriteriaObject().get(key);
        assertNotNull(document);
        assertEquals(Stream.of(value.split("(?<!\\\\),")).map(this::cleanValue).collect(Collectors.toList()), document.get("$in"));
    }

    @Test
    void toCriteria_inNumber() {
        String key = "age";
        String value = "33.5,36.3";
        SearchCriteria searchCriteria = getSearchCriteria(key, value, SearchOp.EQ, false);
        Criteria criteria = CriteriaBuilder.builder()
                .searchCriteria(searchCriteria)
                .build()
                .toCriteria();

        assertNotNull(criteria);

        Document criteriaObject = criteria.getCriteriaObject();
        assertNotNull(criteriaObject);

        BasicDBList basicDBList = criteriaObject.get("$or", BasicDBList.class);
        assertEquals(2, basicDBList.size());

        Document stringDocument = (Document) ((Document) basicDBList.get(0)).get(key);
        Document numberDocument = (Document) ((Document) basicDBList.get(1)).get(key);
        assertEquals(Stream.of(value.split("(?<!\\\\),")).collect(Collectors.toList()), stringDocument.get("$in"));
        assertEquals(Stream.of(value.split("(?<!\\\\),")).map(Double::valueOf).collect(Collectors.toList()), numberDocument.get("$in"));
    }

    @Test
    void toCriteria_inBoolean() {
        String key = "married";
        String value = "true,false";
        SearchCriteria searchCriteria = getSearchCriteria(key, value, SearchOp.EQ, false);
        Criteria criteria = CriteriaBuilder.builder()
                .searchCriteria(searchCriteria)
                .build()
                .toCriteria();

        assertNotNull(criteria);

        Document criteriaObject = criteria.getCriteriaObject();
        assertNotNull(criteriaObject);

        BasicDBList basicDBList = criteriaObject.get("$or", BasicDBList.class);
        assertEquals(2, basicDBList.size());

        Document stringDocument = (Document) ((Document) basicDBList.get(0)).get(key);
        Document booleanDocument = (Document) ((Document) basicDBList.get(1)).get(key);
        assertEquals(Stream.of(value.split("(?<!\\\\),")).collect(Collectors.toList()), stringDocument.get("$in"));
        assertEquals(Stream.of(value.split("(?<!\\\\),")).map(Boolean::valueOf).collect(Collectors.toList()), booleanDocument.get("$in"));
    }

    @Test
    void toCriteria_inDate() {
        String key = "birthDate";
        String value = "1984-11-27T05:36:32Z,1981-12-24T09:31:00.000Z,1984-10-22T01:37:52.000+01:00,2000-10-01";
        SearchCriteria searchCriteria = getSearchCriteria(key, value, SearchOp.EQ, false);
        Criteria criteria = CriteriaBuilder.builder()
                .searchCriteria(searchCriteria)
                .build()
                .toCriteria();

        assertNotNull(criteria);

        Document criteriaObject = criteria.getCriteriaObject();
        assertNotNull(criteriaObject);

        BasicDBList basicDBList = criteriaObject.get("$or", BasicDBList.class);
        assertEquals(2, basicDBList.size());

        Document stringDocument = (Document) ((Document) basicDBList.get(0)).get(key);
        Document dateDocument = (Document) ((Document) basicDBList.get(1)).get(key);
        assertEquals(Stream.of(value.split("(?<!\\\\),")).collect(Collectors.toList()), stringDocument.get("$in"));
        assertEquals(Stream.of(value.split("(?<!\\\\),")).map(this::getDateValue).collect(Collectors.toList()), dateDocument.get("$in"));
    }

    @Test
    void toCriteria_neString() {
        String key = "name";
        String value = "myName";

        SearchCriteria searchCriteria = getSearchCriteria(key, value, SearchOp.NE, false);
        Criteria criteria = CriteriaBuilder.builder()
                .searchCriteria(searchCriteria)
                .build()
                .toCriteria();

        assertNotNull(criteria);

        Document criteriaObject = (Document) criteria.getCriteriaObject().get(key);
        assertNotNull(criteriaObject);
        assertEquals(value, criteriaObject.getString("$ne"));
    }

    @Test
    void toCriteria_neNumber() {
        String key = "age";
        String value = "33.5";
        SearchCriteria searchCriteria = getSearchCriteria(key, value, SearchOp.NE, false);
        Criteria criteria = CriteriaBuilder.builder()
                .searchCriteria(searchCriteria)
                .build()
                .toCriteria();

        assertNotNull(criteria);

        Document criteriaObject = criteria.getCriteriaObject();
        assertNotNull(criteriaObject);

        BasicDBList basicDBList = criteriaObject.get("$or", BasicDBList.class);
        assertEquals(2, basicDBList.size());

        Document stringDocument = (Document) ((Document) basicDBList.get(0)).get(key);
        Document numberDocument = (Document) ((Document) basicDBList.get(1)).get(key);
        assertEquals(value, stringDocument.getString("$ne"));
        assertEquals(Double.valueOf(value), numberDocument.getDouble("$ne"));
    }

    @Test
    void toCriteria_neBoolean() {
        String key = "married";
        String value = "true";
        SearchCriteria searchCriteria = getSearchCriteria(key, value, SearchOp.NE, false);
        Criteria criteria = CriteriaBuilder.builder()
                .searchCriteria(searchCriteria)
                .build()
                .toCriteria();

        assertNotNull(criteria);

        Document criteriaObject = criteria.getCriteriaObject();
        assertNotNull(criteriaObject);

        BasicDBList or = criteriaObject.get("$or", BasicDBList.class);
        assertNotNull(or);
        assertEquals(2, or.size());

        Document stringDocument = (Document) ((Document) or.get(0)).get(key);
        Document booleanDocument = (Document) ((Document) or.get(1)).get(key);
        assertEquals(value, stringDocument.getString("$ne"));
        assertEquals(Boolean.valueOf(value), booleanDocument.getBoolean("$ne"));
    }

    @Test
    void toCriteria_neDate() {
        String key = "birthDate";
        String value = "1984-11-27T05:36:32.00Z";
        SearchCriteria searchCriteria = getSearchCriteria(key, value, SearchOp.NE, false);
        Criteria criteria = CriteriaBuilder.builder()
                .searchCriteria(searchCriteria)
                .build()
                .toCriteria();

        assertNotNull(criteria);

        Document criteriaObject = criteria.getCriteriaObject();
        assertNotNull(criteriaObject);

        BasicDBList or = criteriaObject.get("$or", BasicDBList.class);
        assertNotNull(or);
        assertEquals(2, or.size());

        Document stringDocument = (Document) ((Document) or.get(0)).get(key);
        Document dateDocument = (Document) ((Document) or.get(1)).get(key);
        assertEquals(value, stringDocument.getString("$ne"));
        assertEquals(getDateValue(value), dateDocument.get("$ne"));
    }

    @Test
    void toCriteria_ninString() {
        String key = "name";
        String value = "myName1,myName2,myNa\\,me3";

        SearchCriteria searchCriteria = getSearchCriteria(key, value, SearchOp.NE, false);
        Criteria criteria = CriteriaBuilder.builder()
                .searchCriteria(searchCriteria)
                .build()
                .toCriteria();

        assertNotNull(criteria);

        Document document = (Document) criteria.getCriteriaObject().get(key);
        assertNotNull(document);
        assertEquals(Stream.of(value.split("(?<!\\\\),")).map(this::cleanValue).collect(Collectors.toList()), document.get("$nin"));
    }

    @Test
    void toCriteria_ninNumber() {
        String key = "age";
        String value = "33.5,36.3";
        SearchCriteria searchCriteria = getSearchCriteria(key, value, SearchOp.NE, false);
        Criteria criteria = CriteriaBuilder.builder()
                .searchCriteria(searchCriteria)
                .build()
                .toCriteria();

        assertNotNull(criteria);

        Document criteriaObject = criteria.getCriteriaObject();
        assertNotNull(criteriaObject);

        BasicDBList basicDBList = criteriaObject.get("$or", BasicDBList.class);
        assertEquals(2, basicDBList.size());

        Document stringDocument = (Document) ((Document) basicDBList.get(0)).get(key);
        Document numberDocument = (Document) ((Document) basicDBList.get(1)).get(key);
        assertEquals(Stream.of(value.split("(?<!\\\\),")).collect(Collectors.toList()), stringDocument.get("$nin"));
        assertEquals(Stream.of(value.split("(?<!\\\\),")).map(Double::valueOf).collect(Collectors.toList()), numberDocument.get("$nin"));
    }

    @Test
    void toCriteria_ninBoolean() {
        String key = "married";
        String value = "true,false";
        SearchCriteria searchCriteria = getSearchCriteria(key, value, SearchOp.NE, false);
        Criteria criteria = CriteriaBuilder.builder()
                .searchCriteria(searchCriteria)
                .build()
                .toCriteria();

        assertNotNull(criteria);

        Document criteriaObject = criteria.getCriteriaObject();
        assertNotNull(criteriaObject);

        BasicDBList basicDBList = criteriaObject.get("$or", BasicDBList.class);
        assertEquals(2, basicDBList.size());

        Document stringDocument = (Document) ((Document) basicDBList.get(0)).get(key);
        Document booleanDocument = (Document) ((Document) basicDBList.get(1)).get(key);
        assertEquals(Stream.of(value.split("(?<!\\\\),")).collect(Collectors.toList()), stringDocument.get("$nin"));
        assertEquals(Stream.of(value.split("(?<!\\\\),")).map(Boolean::valueOf).collect(Collectors.toList()), booleanDocument.get("$nin"));
    }

    @Test
    void toCriteria_ninDate() {
        String key = "birthDate";
        String value = "1984-11-27T05:36:32Z,1981-12-24T09:31:00.000Z,1984-10-22T01:37:52.000+01:00,2000-10-01T01:37:52+01:00";
        SearchCriteria searchCriteria = getSearchCriteria(key, value, SearchOp.NE, false);
        Criteria criteria = CriteriaBuilder.builder()
                .searchCriteria(searchCriteria)
                .build()
                .toCriteria();

        assertNotNull(criteria);

        Document criteriaObject = criteria.getCriteriaObject();
        assertNotNull(criteriaObject);

        BasicDBList basicDBList = criteriaObject.get("$or", BasicDBList.class);
        assertEquals(2, basicDBList.size());

        Document stringDocument = (Document) ((Document) basicDBList.get(0)).get(key);
        Document dateDocument = (Document) ((Document) basicDBList.get(1)).get(key);
        assertEquals(Stream.of(value.split("(?<!\\\\),")).collect(Collectors.toList()), stringDocument.get("$nin"));
        assertEquals(Stream.of(value.split("(?<!\\\\),")).map(this::getDateValue).collect(Collectors.toList()), dateDocument.get("$nin"));
    }

    @Test
    void toCriteria_gtString() {
        String key = "name";
        String value = "m";

        SearchCriteria searchCriteria = getSearchCriteria(key, value, SearchOp.GT, false);
        Criteria criteria = CriteriaBuilder.builder()
                .searchCriteria(searchCriteria)
                .build()
                .toCriteria();

        assertNotNull(criteria);

        Document criteriaObject = (Document) criteria.getCriteriaObject().get(key);
        assertNotNull(criteriaObject);
        assertEquals(value, criteriaObject.getString("$gt"));
    }

    @Test
    void toCriteria_gtNumber() {
        String key = "age";
        String value = "33.5";
        SearchCriteria searchCriteria = getSearchCriteria(key, value, SearchOp.GT, false);
        Criteria criteria = CriteriaBuilder.builder()
                .searchCriteria(searchCriteria)
                .build()
                .toCriteria();

        assertNotNull(criteria);

        Document criteriaObject = criteria.getCriteriaObject();
        assertNotNull(criteriaObject);

        BasicDBList basicDBList = criteriaObject.get("$or", BasicDBList.class);
        assertEquals(2, basicDBList.size());

        Document stringDocument = (Document) ((Document) basicDBList.get(0)).get(key);
        Document numberDocument = (Document) ((Document) basicDBList.get(1)).get(key);
        assertEquals(value, stringDocument.getString("$gt"));
        assertEquals(Double.valueOf(value), numberDocument.getDouble("$gt"));
    }

    @Test
    void toCriteria_gtBoolean() {
        String key = "married";
        String value = "true";
        SearchCriteria searchCriteria = getSearchCriteria(key, value, SearchOp.GT, false);
        Criteria criteria = CriteriaBuilder.builder()
                .searchCriteria(searchCriteria)
                .build()
                .toCriteria();

        assertNotNull(criteria);

        Document criteriaObject = criteria.getCriteriaObject();
        assertNotNull(criteriaObject);

        BasicDBList basicDBList = criteriaObject.get("$or", BasicDBList.class);
        assertEquals(2, basicDBList.size());

        Document stringDocument = (Document) ((Document) basicDBList.get(0)).get(key);
        Document booleanDocument = (Document) ((Document) basicDBList.get(1)).get(key);
        assertEquals(value, stringDocument.getString("$gt"));
        assertEquals(Boolean.valueOf(value), booleanDocument.getBoolean("$gt"));
    }

    @Test
    void toCriteria_gtDate() {
        String key = "birthDate";
        String value = "1984-11-27T05:36:32.00Z";
        SearchCriteria searchCriteria = getSearchCriteria(key, value, SearchOp.GT, false);
        Criteria criteria = CriteriaBuilder.builder()
                .searchCriteria(searchCriteria)
                .build()
                .toCriteria();

        assertNotNull(criteria);

        Document criteriaObject = criteria.getCriteriaObject();
        assertNotNull(criteriaObject);

        BasicDBList basicDBList = criteriaObject.get("$or", BasicDBList.class);
        assertEquals(2, basicDBList.size());

        Document stringDocument = (Document) ((Document) basicDBList.get(0)).get(key);
        Document dateDocument = (Document) ((Document) basicDBList.get(1)).get(key);
        assertEquals(value, stringDocument.getString("$gt"));
        assertEquals(getDateValue(value), dateDocument.get("$gt"));
    }

    @Test
    void toCriteria_geString() {
        String key = "name";
        String value = "m";

        SearchCriteria searchCriteria = getSearchCriteria(key, value, SearchOp.GE, false);
        Criteria criteria = CriteriaBuilder.builder()
                .searchCriteria(searchCriteria)
                .build()
                .toCriteria();

        assertNotNull(criteria);

        Document criteriaObject = (Document) criteria.getCriteriaObject().get(key);
        assertNotNull(criteriaObject);
        assertEquals(value, criteriaObject.getString("$gte"));
    }

    @Test
    void toCriteria_geNumber() {
        String key = "age";
        String value = "33.5";
        SearchCriteria searchCriteria = getSearchCriteria(key, value, SearchOp.GE, false);
        Criteria criteria = CriteriaBuilder.builder()
                .searchCriteria(searchCriteria)
                .build()
                .toCriteria();

        assertNotNull(criteria);

        Document criteriaObject = criteria.getCriteriaObject();
        assertNotNull(criteriaObject);

        BasicDBList basicDBList = criteriaObject.get("$or", BasicDBList.class);
        assertEquals(2, basicDBList.size());

        Document stringDocument = (Document) ((Document) basicDBList.get(0)).get(key);
        Document numberDocument = (Document) ((Document) basicDBList.get(1)).get(key);
        assertEquals(value, stringDocument.getString("$gte"));
        assertEquals(Double.valueOf(value), numberDocument.getDouble("$gte"));
    }

    @Test
    void toCriteria_geBoolean() {
        String key = "married";
        String value = "true";
        SearchCriteria searchCriteria = getSearchCriteria(key, value, SearchOp.GE, false);
        Criteria criteria = CriteriaBuilder.builder()
                .searchCriteria(searchCriteria)
                .build()
                .toCriteria();

        assertNotNull(criteria);

        Document criteriaObject = criteria.getCriteriaObject();
        assertNotNull(criteriaObject);

        BasicDBList basicDBList = criteriaObject.get("$or", BasicDBList.class);
        assertEquals(2, basicDBList.size());

        Document stringDocument = (Document) ((Document) basicDBList.get(0)).get(key);
        Document booleanDocument = (Document) ((Document) basicDBList.get(1)).get(key);
        assertEquals(value, stringDocument.getString("$gte"));
        assertEquals(Boolean.valueOf(value), booleanDocument.getBoolean("$gte"));
    }

    @Test
    void toCriteria_geDate() {
        String key = "birthDate";
        String value = "1984-11-27T05:36:32.00Z";
        SearchCriteria searchCriteria = getSearchCriteria(key, value, SearchOp.GE, false);
        Criteria criteria = CriteriaBuilder.builder()
                .searchCriteria(searchCriteria)
                .build()
                .toCriteria();

        assertNotNull(criteria);

        Document criteriaObject = criteria.getCriteriaObject();
        assertNotNull(criteriaObject);

        BasicDBList basicDBList = criteriaObject.get("$or", BasicDBList.class);
        assertEquals(2, basicDBList.size());

        Document stringDocument = (Document) ((Document) basicDBList.get(0)).get(key);
        Document dateDocument = (Document) ((Document) basicDBList.get(1)).get(key);
        assertEquals(value, stringDocument.getString("$gte"));
        assertEquals(getDateValue(value), dateDocument.get("$gte"));
    }

    @Test
    void toCriteria_ltString() {
        String key = "name";
        String value = "m";

        SearchCriteria searchCriteria = getSearchCriteria(key, value, SearchOp.LT, false);
        Criteria criteria = CriteriaBuilder.builder()
                .searchCriteria(searchCriteria)
                .build()
                .toCriteria();

        assertNotNull(criteria);

        Document criteriaObject = (Document) criteria.getCriteriaObject().get(key);
        assertNotNull(criteriaObject);
        assertEquals(value, criteriaObject.getString("$lt"));
    }

    @Test
    void toCriteria_ltNumber() {
        String key = "age";
        String value = "33.5";
        SearchCriteria searchCriteria = getSearchCriteria(key, value, SearchOp.LT, false);
        Criteria criteria = CriteriaBuilder.builder()
                .searchCriteria(searchCriteria)
                .build()
                .toCriteria();

        assertNotNull(criteria);

        Document criteriaObject = criteria.getCriteriaObject();
        assertNotNull(criteriaObject);

        BasicDBList basicDBList = criteriaObject.get("$or", BasicDBList.class);
        assertEquals(2, basicDBList.size());

        Document stringDocument = (Document) ((Document) basicDBList.get(0)).get(key);
        Document numberDocument = (Document) ((Document) basicDBList.get(1)).get(key);
        assertEquals(value, stringDocument.getString("$lt"));
        assertEquals(Double.valueOf(value), numberDocument.getDouble("$lt"));
    }

    @Test
    void toCriteria_ltBoolean() {
        String key = "married";
        String value = "true";
        SearchCriteria searchCriteria = getSearchCriteria(key, value, SearchOp.LT, false);
        Criteria criteria = CriteriaBuilder.builder()
                .searchCriteria(searchCriteria)
                .build()
                .toCriteria();

        assertNotNull(criteria);

        Document criteriaObject = criteria.getCriteriaObject();
        assertNotNull(criteriaObject);

        BasicDBList basicDBList = criteriaObject.get("$or", BasicDBList.class);
        assertEquals(2, basicDBList.size());

        Document stringDocument = (Document) ((Document) basicDBList.get(0)).get(key);
        Document booleanDocument = (Document) ((Document) basicDBList.get(1)).get(key);
        assertEquals(value, stringDocument.getString("$lt"));
        assertEquals(Boolean.valueOf(value), booleanDocument.getBoolean("$lt"));
    }

    @Test
    void toCriteria_ltDate() {
        String key = "birthDate";
        String value = "1984-11-27T05:36:32.00Z";
        SearchCriteria searchCriteria = getSearchCriteria(key, value, SearchOp.LT, false);
        Criteria criteria = CriteriaBuilder.builder()
                .searchCriteria(searchCriteria)
                .build()
                .toCriteria();

        assertNotNull(criteria);

        Document criteriaObject = criteria.getCriteriaObject();
        assertNotNull(criteriaObject);

        BasicDBList basicDBList = criteriaObject.get("$or", BasicDBList.class);
        assertEquals(2, basicDBList.size());

        Document stringDocument = (Document) ((Document) basicDBList.get(0)).get(key);
        Document dateDocument = (Document) ((Document) basicDBList.get(1)).get(key);
        assertEquals(value, stringDocument.getString("$lt"));
        assertEquals(getDateValue(value), dateDocument.get("$lt"));
    }

    @Test
    void toCriteria_leString() {
        String key = "name";
        String value = "m";

        SearchCriteria searchCriteria = getSearchCriteria(key, value, SearchOp.LE, false);
        Criteria criteria = CriteriaBuilder.builder()
                .searchCriteria(searchCriteria)
                .build()
                .toCriteria();

        assertNotNull(criteria);

        Document criteriaObject = (Document) criteria.getCriteriaObject().get(key);
        assertNotNull(criteriaObject);
        assertEquals(value, criteriaObject.getString("$lte"));
    }

    @Test
    void toCriteria_leNumber() {
        String key = "age";
        String value = "33.5";
        SearchCriteria searchCriteria = getSearchCriteria(key, value, SearchOp.LE, false);
        Criteria criteria = CriteriaBuilder.builder()
                .searchCriteria(searchCriteria)
                .build()
                .toCriteria();

        assertNotNull(criteria);

        Document criteriaObject = criteria.getCriteriaObject();
        assertNotNull(criteriaObject);

        BasicDBList basicDBList = criteriaObject.get("$or", BasicDBList.class);
        assertEquals(2, basicDBList.size());

        Document stringDocument = (Document) ((Document) basicDBList.get(0)).get(key);
        Document numberDocument = (Document) ((Document) basicDBList.get(1)).get(key);
        assertEquals(value, stringDocument.getString("$lte"));
        assertEquals(Double.valueOf(value), numberDocument.getDouble("$lte"));
    }

    @Test
    void toCriteria_leBoolean() {
        String key = "married";
        String value = "true";
        SearchCriteria searchCriteria = getSearchCriteria(key, value, SearchOp.LE, false);
        Criteria criteria = CriteriaBuilder.builder()
                .searchCriteria(searchCriteria)
                .build()
                .toCriteria();

        assertNotNull(criteria);

        Document criteriaObject = criteria.getCriteriaObject();
        assertNotNull(criteriaObject);

        BasicDBList basicDBList = criteriaObject.get("$or", BasicDBList.class);
        assertEquals(2, basicDBList.size());

        Document stringDocument = (Document) ((Document) basicDBList.get(0)).get(key);
        Document booleanDocument = (Document) ((Document) basicDBList.get(1)).get(key);
        assertEquals(value, stringDocument.getString("$lte"));
        assertEquals(Boolean.valueOf(value), booleanDocument.getBoolean("$lte"));
    }

    @Test
    void toCriteria_leDate() {
        String key = "birthDate";
        String value = "1984-11-27T05:36:32.00Z";
        SearchCriteria searchCriteria = getSearchCriteria(key, value, SearchOp.LE, false);
        Criteria criteria = CriteriaBuilder.builder()
                .searchCriteria(searchCriteria)
                .build()
                .toCriteria();

        assertNotNull(criteria);

        Document criteriaObject = criteria.getCriteriaObject();
        assertNotNull(criteriaObject);

        BasicDBList basicDBList = criteriaObject.get("$or", BasicDBList.class);
        assertEquals(2, basicDBList.size());

        Document stringDocument = (Document) ((Document) basicDBList.get(0)).get(key);
        Document dateDocument = (Document) ((Document) basicDBList.get(1)).get(key);
        assertEquals(value, stringDocument.getString("$lte"));
        assertEquals(getDateValue(value), dateDocument.get("$lte"));
    }

    @Test
    void toCriteria_exists() {
        String key = "name";

        SearchCriteria searchCriteria = getSearchCriteria(key, null, SearchOp.EXISTS, true);
        Criteria criteria = CriteriaBuilder.builder()
                .searchCriteria(searchCriteria)
                .build()
                .toCriteria();

        assertNotNull(criteria);

        Document criteriaObject = (Document) criteria.getCriteriaObject().get(key);
        assertNotNull(criteriaObject);
        assertEquals(true, criteriaObject.getBoolean("$exists"));
    }

    @Test
    void toCriteria_doesntExist() {
        String key = "name";

        SearchCriteria searchCriteria = getSearchCriteria(key, null, SearchOp.EXISTS, false);
        Criteria criteria = CriteriaBuilder.builder()
                .searchCriteria(searchCriteria)
                .build()
                .toCriteria();

        assertNotNull(criteria);

        Document criteriaObject = (Document) criteria.getCriteriaObject().get(key);
        assertNotNull(criteriaObject);
        assertEquals(false, criteriaObject.getBoolean("$exists"));
    }

    @Test
    void toCriteria_eqRegexWithOptions() {
        String key = "name";
        String value = "/.*m.*/gimscxdtu";

        SearchCriteria searchCriteria = getSearchCriteria(key, value, SearchOp.EQ, false);
        Criteria criteria = CriteriaBuilder.builder()
                .searchCriteria(searchCriteria)
                .build()
                .toCriteria();

        assertNotNull(criteria);

        Document criteriaObject = criteria.getCriteriaObject();
        assertNotNull(criteriaObject);

        assertEquals(new BsonRegularExpression(".*m.*", "gimscxdtu"), criteriaObject.get(key, BsonRegularExpression.class));
    }

    @Test
    void toCriteria_neRegexWithOptions() {
        String key = "name";
        String value = "/.*m.*/gimscxdtu";

        SearchCriteria searchCriteria = getSearchCriteria(key, value, SearchOp.NE, false);
        Criteria criteria = CriteriaBuilder.builder()
                .searchCriteria(searchCriteria)
                .build()
                .toCriteria();

        assertNotNull(criteria);

        Document criteriaObject = (Document) criteria.getCriteriaObject().get(key);
        assertNotNull(criteriaObject);
        assertEquals(new BsonRegularExpression(".*m.*", "gimscxdtu"), criteriaObject.get("$not", BsonRegularExpression.class));
    }

    @Test
    void toCriteria_eqRegexWithoutOptions() {
        String key = "name";
        String value = "/.*m.*";

        SearchCriteria searchCriteria = getSearchCriteria(key, value, SearchOp.EQ, false);
        Criteria criteria = CriteriaBuilder.builder()
                .searchCriteria(searchCriteria)
                .build()
                .toCriteria();

        assertNotNull(criteria);

        Document criteriaObject = criteria.getCriteriaObject();
        assertNotNull(criteriaObject);

        assertEquals(new BsonRegularExpression(".*m.*"), criteriaObject.get(key, BsonRegularExpression.class));
    }

    @Test
    void toCriteria_neRegexWithoutOptions() {
        String key = "name";
        String value = "/.*m.*";

        SearchCriteria searchCriteria = getSearchCriteria(key, value, SearchOp.NE, false);
        Criteria criteria = CriteriaBuilder.builder()
                .searchCriteria(searchCriteria)
                .build()
                .toCriteria();

        assertNotNull(criteria);

        Document criteriaObject = (Document) criteria.getCriteriaObject().get(key);
        assertNotNull(criteriaObject);
        assertEquals(new BsonRegularExpression(".*m.*"), criteriaObject.get("$not", BsonRegularExpression.class));
    }

    private SearchCriteria getSearchCriteria(String key, String value, SearchOp eq, boolean exists) {
        return SearchCriteria.builder()
                .key(key)
                .value(value)
                .op(eq)
                .exists(exists)
                .build();
    }

    private Instant getDateValue(String value) {
        try {
            return OffsetDateTime.parse(value).toInstant();
        } catch (Exception ignored) {
        }
        return LocalDate.parse(value).atStartOfDay().atOffset(ZoneOffset.UTC).toInstant();
    }

    private String cleanValue(String value) {
        return value.replace("\\,", ",");
    }
}