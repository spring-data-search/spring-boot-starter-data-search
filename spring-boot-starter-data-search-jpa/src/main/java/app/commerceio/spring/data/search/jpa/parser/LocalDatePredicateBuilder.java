package app.commerceio.spring.data.search.jpa.parser;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import java.time.LocalDate;

public class LocalDatePredicateBuilder implements PredicateBuilder<LocalDate> {

    @Override
    public LocalDate parse(Class<?> type, String value) {
        try {
            return LocalDate.parse(value);
        } catch (Exception exception) {
            throw new IllegalArgumentException(exception);
        }
    }

    @Override
    public Predicate gt(Class<?> type, Path<?> path, String key, String value, CriteriaBuilder criteriaBuilder) {
        LocalDate localDate = parse(type, value);
        return criteriaBuilder.greaterThan(path.get(key), localDate);
    }

    @Override
    public Predicate ge(Class<?> type, Path<?> path, String key, String value, CriteriaBuilder criteriaBuilder) {
        LocalDate localDate = parse(type, value);
        return criteriaBuilder.greaterThanOrEqualTo(path.get(key), localDate);
    }

    @Override
    public Predicate lt(Class<?> type, Path<?> path, String key, String value, CriteriaBuilder criteriaBuilder) {
        LocalDate localDate = parse(type, value);
        return criteriaBuilder.lessThan(path.get(key), localDate);
    }

    @Override
    public Predicate le(Class<?> type, Path<?> path, String key, String value, CriteriaBuilder criteriaBuilder) {
        LocalDate localDate = parse(type, value);
        return criteriaBuilder.lessThanOrEqualTo(path.get(key), localDate);
    }
}
