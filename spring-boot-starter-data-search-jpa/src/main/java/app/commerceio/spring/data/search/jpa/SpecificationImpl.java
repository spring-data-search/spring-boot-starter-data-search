package app.commerceio.spring.data.search.jpa;

import app.commerceio.spring.data.search.SearchOp;
import app.commerceio.spring.data.search.jpa.parser.PredicateBuilder;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Builder
@RequiredArgsConstructor
public class SpecificationImpl<T> implements Specification<T> {


    private final boolean exists;
    private final String key;
    private final SearchOp op;
    private final String value;

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        SearchCriteria searchCriteria = SearchCriteria.builder()
                .exists(exists)
                .key(key)
                .op(op)
                .value(value)
                .root(root)
                .build();

        switch (searchCriteria.getOp()) {
            case EQ:
                return eq(searchCriteria, criteriaBuilder);
            case NE:
                return ne(searchCriteria, criteriaBuilder);
            case GT:
                return gt(searchCriteria, criteriaBuilder);
            case GE:
                return ge(searchCriteria, criteriaBuilder);
            case LT:
                return lt(searchCriteria, criteriaBuilder);
            case LE:
                return le(searchCriteria, criteriaBuilder);
            case EXISTS:
            default:
                return exists(searchCriteria, criteriaBuilder);
        }
    }

    private Predicate exists(SearchCriteria searchCriteria, CriteriaBuilder criteriaBuilder) {
        if (searchCriteria.isExists()) {
            return criteriaBuilder.isNotNull(searchCriteria.getPath().get(searchCriteria.getKey()));
        } else {
            return criteriaBuilder.isNull(searchCriteria.getPath().get(searchCriteria.getKey()));
        }
    }

    private Predicate eq(SearchCriteria searchCriteria, CriteriaBuilder criteriaBuilder) {
        if (searchCriteria.isArray()) {
            var values = Stream.of(getValues(searchCriteria.getValue()))
                    .map(this::cleanValue)
                    .collect(Collectors.toList());
            return PredicateBuilder.builder(searchCriteria.getType()).in(searchCriteria.getType(), searchCriteria.getPath(),
                    searchCriteria.getKey(), values, criteriaBuilder);
        } else {
            String cleanValue = cleanValue(searchCriteria.getValue());
            if (searchCriteria.isStartsWith() || searchCriteria.isEndsWith()) {
                return PredicateBuilder.builder(searchCriteria.getType()).like(searchCriteria.getType(), searchCriteria.getPath(),
                        searchCriteria.getKey(), cleanValue, searchCriteria.isStartsWith(), searchCriteria.isEndsWith(), criteriaBuilder);
            } else {
                return PredicateBuilder.builder(searchCriteria.getType()).eq(searchCriteria.getType(), searchCriteria.getPath(),
                        searchCriteria.getKey(), cleanValue, criteriaBuilder);
            }
        }
    }

    private Predicate ne(SearchCriteria searchCriteria, CriteriaBuilder criteriaBuilder) {
        if (searchCriteria.isArray()) {
            var values = Stream.of(getValues(searchCriteria.getValue()))
                    .map(this::cleanValue)
                    .collect(Collectors.toList());
            return PredicateBuilder.builder(searchCriteria.getType()).nin(searchCriteria.getType(), searchCriteria.getPath(),
                    searchCriteria.getKey(), values, criteriaBuilder);
        } else {
            String cleanValue = cleanValue(searchCriteria.getValue());
            if (searchCriteria.isStartsWith() || searchCriteria.isEndsWith()) {
                return PredicateBuilder.builder(searchCriteria.getType()).nlike(searchCriteria.getType(), searchCriteria.getPath(),
                        searchCriteria.getKey(), cleanValue, searchCriteria.isStartsWith(), searchCriteria.isEndsWith(), criteriaBuilder);
            } else {
                return PredicateBuilder.builder(searchCriteria.getType()).ne(searchCriteria.getType(), searchCriteria.getPath(),
                        searchCriteria.getKey(), cleanValue, criteriaBuilder);
            }
        }
    }

    private Predicate gt(SearchCriteria searchCriteria, CriteriaBuilder criteriaBuilder) {
        String cleanValue = cleanValue(searchCriteria.getValue());
        return PredicateBuilder.builder(searchCriteria.getType()).gt(searchCriteria.getType(), searchCriteria.getPath(),
                searchCriteria.getKey(), cleanValue, criteriaBuilder);
    }

    private Predicate ge(SearchCriteria searchCriteria, CriteriaBuilder criteriaBuilder) {
        String cleanValue = cleanValue(searchCriteria.getValue());
        return PredicateBuilder.builder(searchCriteria.getType()).ge(searchCriteria.getType(), searchCriteria.getPath(),
                searchCriteria.getKey(), cleanValue, criteriaBuilder);
    }

    private Predicate lt(SearchCriteria searchCriteria, CriteriaBuilder criteriaBuilder) {
        String cleanValue = cleanValue(searchCriteria.getValue());
        return PredicateBuilder.builder(searchCriteria.getType()).lt(searchCriteria.getType(), searchCriteria.getPath(),
                searchCriteria.getKey(), cleanValue, criteriaBuilder);
    }

    private Predicate le(SearchCriteria searchCriteria, CriteriaBuilder criteriaBuilder) {
        String cleanValue = cleanValue(searchCriteria.getValue());
        return PredicateBuilder.builder(searchCriteria.getType()).le(searchCriteria.getType(), searchCriteria.getPath(),
                searchCriteria.getKey(), cleanValue, criteriaBuilder);
    }

    private String[] getValues(String value) {
        return value.split("(?<!\\\\),");
    }

    private String cleanValue(String value) {
        return value
                .replace("\\,", ",")
                .replace("\\*", "*");
    }
}
