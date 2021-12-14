package app.commerceio.spring.data.search.jpa.parser;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import java.math.BigDecimal;

public class BigDecimalPredicateBuilder implements PredicateBuilder<BigDecimal> {

    @Override
    public BigDecimal parse(Class<?> type, String value) {
        return BigDecimal.valueOf(Double.parseDouble(value));
    }

    @Override
    public Predicate gt(Class<?> type, Path<?> path, String key, String value, CriteriaBuilder criteriaBuilder) {
        BigDecimal bigDecimal = parse(type, value);
        return criteriaBuilder.greaterThan(path.get(key), bigDecimal);
    }

    @Override
    public Predicate ge(Class<?> type, Path<?> path, String key, String value, CriteriaBuilder criteriaBuilder) {
        BigDecimal bigDecimal = parse(type, value);
        return criteriaBuilder.greaterThanOrEqualTo(path.get(key), bigDecimal);
    }

    @Override
    public Predicate lt(Class<?> type, Path<?> path, String key, String value, CriteriaBuilder criteriaBuilder) {
        BigDecimal bigDecimal = parse(type, value);
        return criteriaBuilder.lessThan(path.get(key), bigDecimal);
    }

    @Override
    public Predicate le(Class<?> type, Path<?> path, String key, String value, CriteriaBuilder criteriaBuilder) {
        BigDecimal bigDecimal = parse(type, value);
        return criteriaBuilder.lessThanOrEqualTo(path.get(key), bigDecimal);
    }
}
