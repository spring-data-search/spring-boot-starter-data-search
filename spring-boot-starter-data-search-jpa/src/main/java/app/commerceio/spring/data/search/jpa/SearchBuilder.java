package app.commerceio.spring.data.search.jpa;

import app.commerceio.spring.data.search.Mapper;
import app.commerceio.spring.data.search.SearchLexer;
import app.commerceio.spring.data.search.SearchParser;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

public class SearchBuilder {

    /**
     * Parse the search query.
     *
     * @param search A {@link String} query.
     * @return A {@link Specification} instance.
     */
    public <T> Specification<T> parse(String search) {
        return parse(search, Mapper.flatMapper().build());
    }

    /**
     * Parse the search query.
     *
     * @param search A {@link String} query.
     * @param mapper A {@link Mapper} instance.
     * @return A {@link Specification} instance.
     */
    public <T> Specification<T> parse(String search, Mapper mapper) {
        if (StringUtils.isBlank(search)) {
            return null;
        }

        SearchVisitorImpl<T> searchVisitor = SearchVisitorImpl.<T>builder()
                .mapper(mapper)
                .build();
        var parser = getParser(search);
        return searchVisitor.visit(parser.input());
    }

    private SearchParser getParser(String search) {
        var lexer = new SearchLexer(CharStreams.fromString(search));
        var tokens = new CommonTokenStream(lexer);
        return new SearchParser(tokens);
    }
}
