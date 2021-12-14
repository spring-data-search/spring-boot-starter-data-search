package app.commerceio.spring.data.search.jpa.parser;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import java.time.OffsetTime;

public class OffsetTimePredicateBuilder implements PredicateBuilder<OffsetTime> {

    @Override
    public OffsetTime parse(Class<?> type, String value) {
        try {
            return OffsetTime.parse(value);
        } catch (Exception exception) {
            throw new IllegalArgumentException(exception);
        }
    }

    @Override
    public Predicate gt(Class<?> type, Path<?> path, String key, String value, CriteriaBuilder criteriaBuilder) {
        OffsetTime offsetTime = parse(type, value);
        return criteriaBuilder.greaterThan(path.get(key), offsetTime);
    }

    @Override
    public Predicate ge(Class<?> type, Path<?> path, String key, String value, CriteriaBuilder criteriaBuilder) {
        OffsetTime offsetTime = parse(type, value);
        return criteriaBuilder.greaterThanOrEqualTo(path.get(key), offsetTime);
    }

    @Override
    public Predicate lt(Class<?> type, Path<?> path, String key, String value, CriteriaBuilder criteriaBuilder) {
        OffsetTime offsetTime = parse(type, value);
        return criteriaBuilder.lessThan(path.get(key), offsetTime);
    }

    @Override
    public Predicate le(Class<?> type, Path<?> path, String key, String value, CriteriaBuilder criteriaBuilder) {
        OffsetTime offsetTime = parse(type, value);
        return criteriaBuilder.lessThanOrEqualTo(path.get(key), offsetTime);
    }
}
