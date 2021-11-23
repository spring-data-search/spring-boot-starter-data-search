package app.commerceio.spring.data.search.jpa.parser;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import java.time.LocalDateTime;

public class LocalDateTimePredicateBuilder implements PredicateBuilder<LocalDateTime> {

    @Override
    public LocalDateTime parse(String value) {
        try {
            return LocalDateTime.parse(value);
        } catch (Exception exception) {
            throw new IllegalArgumentException(exception);
        }
    }

    @Override
    public Predicate gt(Path<?> path, String key, String value, CriteriaBuilder criteriaBuilder) {
        LocalDateTime localDateTime = parse(value);
        return criteriaBuilder.greaterThan(path.get(key), localDateTime);
    }

    @Override
    public Predicate ge(Path<?> path, String key, String value, CriteriaBuilder criteriaBuilder) {
        LocalDateTime localDateTime = parse(value);
        return criteriaBuilder.greaterThanOrEqualTo(path.get(key), localDateTime);
    }

    @Override
    public Predicate lt(Path<?> path, String key, String value, CriteriaBuilder criteriaBuilder) {
        LocalDateTime localDateTime = parse(value);
        return criteriaBuilder.lessThan(path.get(key), localDateTime);
    }

    @Override
    public Predicate le(Path<?> path, String key, String value, CriteriaBuilder criteriaBuilder) {
        LocalDateTime localDateTime = parse(value);
        return criteriaBuilder.lessThanOrEqualTo(path.get(key), localDateTime);
    }
}
