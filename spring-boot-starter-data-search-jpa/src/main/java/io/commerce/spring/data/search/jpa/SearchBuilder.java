package io.commerce.spring.data.search.jpa;

import io.commerce.spring.data.search.SearchVisitor;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

public class SearchBuilder {

    public <T> Specification<T> parse(Class<T> type, String search) {
        if (type != null && StringUtils.isBlank(search)) {
            return null;
        }

        SearchVisitor<Specification<T>> searchVisitor = new SearchVisitorImpl<>();
        var parser = getParser(search);
        return searchVisitor.visit(parser.input());
    }

    private io.commerce.spring.data.search.SearchParser getParser(String search) {
        var lexer = new io.commerce.spring.data.search.SearchLexer(CharStreams.fromString(search));
        var tokens = new CommonTokenStream(lexer);
        return new io.commerce.spring.data.search.SearchParser(tokens);
    }
}
