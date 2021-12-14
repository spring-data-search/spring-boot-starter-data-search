package app.commerceio.spring.data.search.jpa.parser;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import java.time.LocalDateTime;

public class LocalDateTimePredicateBuilder implements PredicateBuilder<LocalDateTime> {

    @Override
    public LocalDateTime parse(Class<?> type, String value) {
        try {
            return LocalDateTime.parse(value);
        } catch (Exception exception) {
            throw new IllegalArgumentException(exception);
        }
    }

    @Override
    public Predicate gt(Class<?> type, Path<?> path, String key, String value, CriteriaBuilder criteriaBuilder) {
        LocalDateTime localDateTime = parse(type, value);
        return criteriaBuilder.greaterThan(path.get(key), localDateTime);
    }

    @Override
    public Predicate ge(Class<?> type, Path<?> path, String key, String value, CriteriaBuilder criteriaBuilder) {
        LocalDateTime localDateTime = parse(type, value);
        return criteriaBuilder.greaterThanOrEqualTo(path.get(key), localDateTime);
    }

    @Override
    public Predicate lt(Class<?> type, Path<?> path, String key, String value, CriteriaBuilder criteriaBuilder) {
        LocalDateTime localDateTime = parse(type, value);
        return criteriaBuilder.lessThan(path.get(key), localDateTime);
    }

    @Override
    public Predicate le(Class<?> type, Path<?> path, String key, String value, CriteriaBuilder criteriaBuilder) {
        LocalDateTime localDateTime = parse(type, value);
        return criteriaBuilder.lessThanOrEqualTo(path.get(key), localDateTime);
    }
}
