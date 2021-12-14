package app.commerceio.spring.data.search.jpa.parser;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;

public class BooleanPredicateBuilder implements PredicateBuilder<Boolean> {

    @Override
    public Boolean parse(Class<?> type, String value) {
        return Boolean.valueOf(value);
    }

    @Override
    public Predicate gt(Class<?> type, Path<?> path, String key, String value, CriteriaBuilder criteriaBuilder) {
        Boolean aBoolean = parse(type, value);
        return criteriaBuilder.greaterThan(path.get(key), aBoolean);
    }

    @Override
    public Predicate ge(Class<?> type, Path<?> path, String key, String value, CriteriaBuilder criteriaBuilder) {
        Boolean aBoolean = parse(type, value);
        return criteriaBuilder.greaterThanOrEqualTo(path.get(key), aBoolean);
    }

    @Override
    public Predicate lt(Class<?> type, Path<?> path, String key, String value, CriteriaBuilder criteriaBuilder) {
        Boolean aBoolean = parse(type, value);
        return criteriaBuilder.lessThan(path.get(key), aBoolean);
    }

    @Override
    public Predicate le(Class<?> type, Path<?> path, String key, String value, CriteriaBuilder criteriaBuilder) {
        Boolean aBoolean = parse(type, value);
        return criteriaBuilder.lessThanOrEqualTo(path.get(key), aBoolean);
    }
}
