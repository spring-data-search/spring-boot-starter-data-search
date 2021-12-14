package app.commerceio.spring.data.search.jpa.parser;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;

public class EnumPredicateBuilder implements PredicateBuilder<Enum<?>> {

    @Override
    @SuppressWarnings({"unchecked"})
    public Enum<?> parse(Class type, String value) {
        return Enum.valueOf(type, value);
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public Predicate gt(Class<?> type, Path<?> path, String key, String value, CriteriaBuilder criteriaBuilder) {
        Enum anEnum = parse(type, value);
        return criteriaBuilder.greaterThan(path.get(key), anEnum);
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public Predicate ge(Class<?> type, Path<?> path, String key, String value, CriteriaBuilder criteriaBuilder) {
        Enum anEnum = parse(type, value);
        return criteriaBuilder.greaterThanOrEqualTo(path.get(key), anEnum);
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public Predicate lt(Class<?> type, Path<?> path, String key, String value, CriteriaBuilder criteriaBuilder) {
        Enum anEnum = parse(type, value);
        return criteriaBuilder.lessThan(path.get(key), anEnum);
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public Predicate le(Class<?> type, Path<?> path, String key, String value, CriteriaBuilder criteriaBuilder) {
        Enum anEnum = parse(type, value);
        return criteriaBuilder.lessThanOrEqualTo(path.get(key), anEnum);
    }
}
