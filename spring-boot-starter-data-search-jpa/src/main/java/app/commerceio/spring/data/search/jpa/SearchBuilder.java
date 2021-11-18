package app.commerceio.spring.data.search.jpa;

import app.commerceio.spring.data.search.SearchLexer;
import app.commerceio.spring.data.search.SearchParser;
import app.commerceio.spring.data.search.SearchVisitor;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

public class SearchBuilder {

    public <T> Specification<T> parse(String search) {
        if (StringUtils.isBlank(search)) {
            return null;
        }

        SearchVisitor<Specification<T>> searchVisitor = new SearchVisitorImpl<>();
        var parser = getParser(search);
        return searchVisitor.visit(parser.input());
    }

    private SearchParser getParser(String search) {
        var lexer = new SearchLexer(CharStreams.fromString(search));
        var tokens = new CommonTokenStream(lexer);
        return new SearchParser(tokens);
    }
}
