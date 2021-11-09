package app.commerceio.spring.data.search.mongodb;

import app.commerceio.spring.data.search.SearchLexer;
import app.commerceio.spring.data.search.SearchParser;
import app.commerceio.spring.data.search.SearchVisitor;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.mongodb.core.query.Criteria;

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

        return new Criteria().andOperator(criteria);
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

        return new Criteria().orOperator(criteria);
    }

    private SearchParser getParser(String search) {
        var lexer = new SearchLexer(CharStreams.fromString(search));
        var tokens = new CommonTokenStream(lexer);
        return new SearchParser(tokens);
    }
}
