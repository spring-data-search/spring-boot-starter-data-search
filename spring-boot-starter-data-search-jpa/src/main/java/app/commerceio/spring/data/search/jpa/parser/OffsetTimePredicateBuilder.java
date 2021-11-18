package app.commerceio.spring.data.search.jpa.parser;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import java.time.OffsetTime;

public class OffsetTimePredicateBuilder implements PredicateBuilder<OffsetTime> {

    @Override
    public OffsetTime parse(String value) {
        try {
            return OffsetTime.parse(value);
        } catch (Exception exception) {
            throw new IllegalArgumentException(exception);
        }
    }

    @Override
    public Predicate gt(Path<?> path, String key, String value, CriteriaBuilder criteriaBuilder) {
        return criteriaBuilder.greaterThan(path.get(key), parse(value));
    }

    @Override
    public Predicate ge(Path<?> path, String key, String value, CriteriaBuilder criteriaBuilder) {
        return criteriaBuilder.greaterThanOrEqualTo(path.get(key), parse(value));
    }

    @Override
    public Predicate lt(Path<?> path, String key, String value, CriteriaBuilder criteriaBuilder) {
        return criteriaBuilder.lessThan(path.get(key), parse(value));
    }

    @Override
    public Predicate le(Path<?> path, String key, String value, CriteriaBuilder criteriaBuilder) {
        return criteriaBuilder.lessThanOrEqualTo(path.get(key), parse(value));
    }
}
