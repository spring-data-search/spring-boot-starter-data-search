package io.commerce.spring.data.search.mongodb;

import io.commerce.spring.data.search.SearchVisitor;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

public class SearchBuilder {

    public Criteria parse(String search) {
        if (StringUtils.isBlank(search)) {
            return null;
        }

        SearchVisitor<Criteria> searchVisitor = new SearchVisitorImpl();
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

    private io.commerce.spring.data.search.SearchParser getParser(String search) {
        var lexer = new io.commerce.spring.data.search.SearchLexer(CharStreams.fromString(search));
        var tokens = new CommonTokenStream(lexer);
        return new io.commerce.spring.data.search.SearchParser(tokens);
    }
}
