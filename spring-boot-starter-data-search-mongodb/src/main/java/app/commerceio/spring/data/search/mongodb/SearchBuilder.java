package app.commerceio.spring.data.search.mongodb;

import app.commerceio.spring.data.search.SearchLexer;
import app.commerceio.spring.data.search.SearchParser;
import app.commerceio.spring.data.search.SearchVisitor;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

public class SearchBuilder {

    /**
     * Parse the search query.
     *
     * @param search A {@link String} query.
     * @return A {@link Criteria} instance.
     */
    public Criteria parse(String search) {
        if (StringUtils.isBlank(search)) {
            return null;
        }

        SearchVisitor<Criteria> searchVisitor = new SearchVisitorImpl();
        var parser = getParser(search);
        return searchVisitor.visit(parser.input());
    }


    /**
     * Combine an array of {@link Criteria} with and operator.
     *
     * @param criteria An array of {@link Criteria}.
     * @return A {@link Criteria} instance.
     */
    public Criteria and(Criteria... criteria) {
        if (criteria == null || criteria.length == 0) {
            return null;
        }
        List<Criteria> criteriaList = stream(criteria)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        return new Criteria().andOperator(criteriaList);
    }

    /**
     * Combine an array of {@link Criteria} with or operator.
     *
     * @param criteria An array of {@link Criteria}.
     * @return A {@link Criteria} instance.
     */
    public Criteria or(Criteria... criteria) {
        if (criteria == null || criteria.length == 0) {
            return null;
        }
        List<Criteria> criteriaList = stream(criteria)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        return new Criteria().orOperator(criteriaList);
    }

    private SearchParser getParser(String search) {
        var lexer = new SearchLexer(CharStreams.fromString(search));
        var tokens = new CommonTokenStream(lexer);
        return new SearchParser(tokens);
    }
}
