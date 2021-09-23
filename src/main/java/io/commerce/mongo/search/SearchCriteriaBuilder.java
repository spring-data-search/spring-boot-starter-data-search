package io.commerce.mongo.search;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

public class SearchCriteriaBuilder {

    private final io.commerce.mongo.search.SearchVisitor<Criteria> searchVisitor;

    public SearchCriteriaBuilder() {
        this.searchVisitor = new SearchVisitorImpl();
    }

    public Criteria parse(String search) {
        if (StringUtils.isBlank(search)) {
            return null;
        }
        var parser = getParser(search);
        return searchVisitor.visit(parser.input());
    }

    public Criteria and(Criteria... criteria) {
        if (criteria == null) {
            return null;
        }
        List<Criteria> criteriaList = stream(criteria)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        return new Criteria().andOperator(criteriaList);
    }

    public Criteria or(Criteria... criteria) {
        if (criteria == null) {
            return null;
        }
        List<Criteria> criteriaList = stream(criteria)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        return new Criteria().orOperator(criteriaList);
    }

    private io.commerce.mongo.search.SearchParser getParser(String search) {
        var lexer = new io.commerce.mongo.search.SearchLexer(CharStreams.fromString(search));
        var tokens = new CommonTokenStream(lexer);
        return new io.commerce.mongo.search.SearchParser(tokens);
    }
}
