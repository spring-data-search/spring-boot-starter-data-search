package app.commerceio.spring.data.search.jpa.parser;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import java.math.BigDecimal;

public class BigDecimalPredicateBuilder implements PredicateBuilder<BigDecimal> {

    @Override
    public BigDecimal parse(String value) {
        return BigDecimal.valueOf(Double.parseDouble(value));
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
