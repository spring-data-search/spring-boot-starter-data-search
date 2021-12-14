package app.commerceio.spring.data.search.jpa.parser;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.util.List;

import static app.commerceio.spring.data.search.jpa.LikePattern.pattern;

public interface PredicateBuilder<T> {

    T parse(Class<?> type, String value);

    static PredicateBuilder<?> builder(Class<?> type) {
        if (LocalDate.class.equals(type)) {
            return new LocalDatePredicateBuilder();
        } else if (LocalDateTime.class.equals(type)) {
            return new LocalDateTimePredicateBuilder();
        } else if (OffsetDateTime.class.equals(type)) {
            return new OffsetDateTimePredicateBuilder();
        } else if (LocalTime.class.equals(type)) {
            return new LocalTimePredicateBuilder();
        } else if (OffsetTime.class.equals(type)) {
            return new OffsetTimePredicateBuilder();
        } else if (Boolean.class.equals(type)
                || boolean.class.equals(type)) {
            return new BooleanPredicateBuilder();
        } else if (BigDecimal.class.equals(type)) {
            return new BigDecimalPredicateBuilder();
        } else if (Number.class.isAssignableFrom(type)
                || double.class.equals(type)
                || int.class.equals(type)
                || long.class.equals(type)
                || float.class.equals(type)) {
            return new NumberPredicateBuilder();
        } else if (Enum.class.isAssignableFrom(type)) {
            return new EnumPredicateBuilder();
        } else {
            return new DefaultPredicateBuilder();
        }
    }

    default Predicate eq(Class<?> type, Path<?> path, String key, String value, CriteriaBuilder criteriaBuilder) {
        return criteriaBuilder.equal(path.get(key), parse(type, value));
    }

    default Predicate ne(Class<?> type, Path<?> path, String key, String value, CriteriaBuilder criteriaBuilder) {
        return criteriaBuilder.notEqual(path.get(key), parse(type, value));
    }

    default Predicate like(Class<?> type, Path<?> path, String key, String value, boolean startsWith, boolean endsWith, CriteriaBuilder criteriaBuilder) {
        String likeValue = MessageFormat.format(pattern(startsWith, endsWith).getPattern(), value);
        return criteriaBuilder.like(path.get(key), likeValue);
    }

    default Predicate nlike(Class<?> type, Path<?> path, String key, String value, boolean startsWith, boolean endsWith, CriteriaBuilder criteriaBuilder) {
        String likeValue = MessageFormat.format(pattern(startsWith, endsWith).getPattern(), value);
        return criteriaBuilder.notLike(path.get(key), likeValue);
    }

    default Predicate gt(Class<?> type, Path<?> path, String key, String value, CriteriaBuilder criteriaBuilder) {
        return criteriaBuilder.greaterThan(path.get(key), value);
    }

    default Predicate ge(Class<?> type, Path<?> path, String key, String value, CriteriaBuilder criteriaBuilder) {
        return criteriaBuilder.greaterThanOrEqualTo(path.get(key), value);
    }

    default Predicate lt(Class<?> type, Path<?> path, String key, String value, CriteriaBuilder criteriaBuilder) {
        return criteriaBuilder.lessThan(path.get(key), value);
    }

    default Predicate le(Class<?> type, Path<?> path, String key, String value, CriteriaBuilder criteriaBuilder) {
        return criteriaBuilder.lessThanOrEqualTo(path.get(key), value);
    }

    default Predicate in(Class<?> type, Path<?> path, String key, List<String> values, CriteriaBuilder criteriaBuilder) {
        CriteriaBuilder.In<T> in = criteriaBuilder.in(path.get(key));
        values.stream().map((String val) -> parse(type, val)).forEach(in::value);
        return in;
    }

    default Predicate nin(Class<?> type, Path<?> path, String key, List<String> values, CriteriaBuilder criteriaBuilder) {
        return criteriaBuilder.not(in(type, path, key, values, criteriaBuilder));
    }
}
