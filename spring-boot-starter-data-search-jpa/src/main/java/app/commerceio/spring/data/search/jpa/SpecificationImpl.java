package app.commerceio.spring.data.search.jpa;

import app.commerceio.spring.data.search.SearchCriteria;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Builder
@RequiredArgsConstructor
public class SpecificationImpl<T> implements Specification<T> {

    private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    private final SearchCriteria searchCriteria;

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        Path<?> path = getPath(root, searchCriteria.getKey());
        String fieldName = getFieldName(searchCriteria.getKey());
        Object value = parseValue(searchCriteria.getValue());
        switch (searchCriteria.getOp()) {
            case EQ:
                return eq(criteriaBuilder, path, fieldName, value);
            case NE:
                return neq(criteriaBuilder, path, fieldName, value);
            case GT:
                return criteriaBuilder.greaterThan(path.get(fieldName), String.valueOf(value));
            case GE:
                return criteriaBuilder.greaterThanOrEqualTo(path.get(fieldName), String.valueOf(value));
            case LT:
                return criteriaBuilder.lessThan(path.get(fieldName), String.valueOf(value));
            case LE:
                return criteriaBuilder.lessThanOrEqualTo(path.get(fieldName), String.valueOf(value));
            case EXISTS:
                if (searchCriteria.isExists()) {
                    return criteriaBuilder.isNotNull(path.get(fieldName));
                } else {
                    return criteriaBuilder.isNull(path.get(fieldName));
                }
            case UNKNOWN:
                return null;
            default:
                throw new IllegalArgumentException();
        }
    }

    private Predicate neq(CriteriaBuilder criteriaBuilder, Path<?> path, String fieldName, Object value) {
        if (value instanceof List) {
            return criteriaBuilder.not(criteriaBuilder.in(path.get(fieldName).in(((List<?>) value).toArray())));
        } else {
            return criteriaBuilder.notEqual(path.get(fieldName), value);
        }
    }

    private Predicate eq(CriteriaBuilder criteriaBuilder, Path<?> path, String fieldName, Object value) {
        if (value instanceof List) {
            return criteriaBuilder.in(path.get(fieldName).in(((List<?>) value).toArray()));
        } else {
            return criteriaBuilder.equal(path.get(fieldName), value);
        }
    }

    private Path<?> getPath(Root<T> root, String key) {
        if (StringUtils.isBlank(key)) {
            return root;
        }
        String[] keyArray = StringUtils.split(key, ".");
        if (keyArray.length <= 1) {
            return root;
        }
        String[] keyList = Arrays.copyOf(keyArray, keyArray.length - 1);
        Path<?> path = root;
        for (String k : keyList) {
            path = path.get(k);
        }
        return path;
    }

    private String getFieldName(String key) {
        List<String> keys = Arrays.asList(StringUtils.split(key, "."));
        return keys.get(keys.size() - 1);
    }

    private Object parseValue(String value) {

        if (value == null) {
            return null;
        }

        String[] parts = StringUtils.split(value, ",");
        if (parts.length > 1) {
            return Stream.of(parts)
                    .map(this::parseValue)
                    .collect(Collectors.toList());
        }

        if ("true".equalsIgnoreCase(value) || "false".equalsIgnoreCase(value)) {
            return Boolean.parseBoolean(value);
        }

        try {
            return parseLocalDateTime(value);
        } catch (DateTimeParseException | IllegalArgumentException ignored) {
        }

        try {
            return NumberFormat.getInstance().parse(value);
        } catch (ParseException ignored) {
        }

        return value;
    }

    private Instant parseLocalDateTime(String value) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
        return LocalDateTime.parse(value, formatter).atZone(ZoneOffset.UTC).toInstant();
    }
}
