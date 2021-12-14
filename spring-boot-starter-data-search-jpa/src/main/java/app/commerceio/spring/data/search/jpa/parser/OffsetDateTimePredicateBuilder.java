package app.commerceio.spring.data.search.jpa.parser;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import java.time.OffsetDateTime;

public class OffsetDateTimePredicateBuilder implements PredicateBuilder<OffsetDateTime> {

    @Override
    public OffsetDateTime parse(Class<?> type, String value) {
        try {
            return OffsetDateTime.parse(value);
        } catch (Exception exception) {
            throw new IllegalArgumentException(exception);
        }
    }

    @Override
    public Predicate gt(Class<?> type, Path<?> path, String key, String value, CriteriaBuilder criteriaBuilder) {
        OffsetDateTime offsetDateTime = parse(type, value);
        return criteriaBuilder.greaterThan(path.get(key), offsetDateTime);
    }

    @Override
    public Predicate ge(Class<?> type, Path<?> path, String key, String value, CriteriaBuilder criteriaBuilder) {
        OffsetDateTime offsetDateTime = parse(type, value);
        return criteriaBuilder.greaterThanOrEqualTo(path.get(key), offsetDateTime);
    }

    @Override
    public Predicate lt(Class<?> type, Path<?> path, String key, String value, CriteriaBuilder criteriaBuilder) {
        OffsetDateTime offsetDateTime = parse(type, value);
        return criteriaBuilder.lessThan(path.get(key), offsetDateTime);
    }

    @Override
    public Predicate le(Class<?> type, Path<?> path, String key, String value, CriteriaBuilder criteriaBuilder) {
        OffsetDateTime offsetDateTime = parse(type, value);
        return criteriaBuilder.lessThanOrEqualTo(path.get(key), offsetDateTime);
    }
}
