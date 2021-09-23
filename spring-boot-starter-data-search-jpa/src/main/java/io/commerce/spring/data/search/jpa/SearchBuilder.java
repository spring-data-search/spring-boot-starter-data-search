package io.commerce.spring.data.search.jpa;

import io.commerce.spring.data.search.SearchLexer;
import io.commerce.spring.data.search.SearchParser;
import io.commerce.spring.data.search.SearchVisitor;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.util.Arrays.spliterator;
import static java.util.Arrays.stream;

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
