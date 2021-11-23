package app.commerceio.spring.data.search.jpa.parser;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;

public class BooleanPredicateBuilder implements PredicateBuilder<Boolean> {

    @Override
    public Boolean parse(String value) {
        return Boolean.valueOf(value);
    }

    @Override
    public Predicate gt(Path<?> path, String key, String value, CriteriaBuilder criteriaBuilder) {
        Boolean aBoolean = parse(value);
        return criteriaBuilder.greaterThan(path.get(key), aBoolean);
    }

    @Override
    public Predicate ge(Path<?> path, String key, String value, CriteriaBuilder criteriaBuilder) {
        Boolean aBoolean = parse(value);
        return criteriaBuilder.greaterThanOrEqualTo(path.get(key), aBoolean);
    }

    @Override
    public Predicate lt(Path<?> path, String key, String value, CriteriaBuilder criteriaBuilder) {
        Boolean aBoolean = parse(value);
        return criteriaBuilder.lessThan(path.get(key), aBoolean);
    }

    @Override
    public Predicate le(Path<?> path, String key, String value, CriteriaBuilder criteriaBuilder) {
        Boolean aBoolean = parse(value);
        return criteriaBuilder.lessThanOrEqualTo(path.get(key), aBoolean);
    }
}
