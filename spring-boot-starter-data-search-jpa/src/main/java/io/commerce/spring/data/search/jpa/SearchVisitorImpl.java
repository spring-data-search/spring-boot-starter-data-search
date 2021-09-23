package io.commerce.spring.data.search.jpa;

import io.commerce.spring.data.search.LogicalOp;
import io.commerce.spring.data.search.SearchBaseVisitor;
import io.commerce.spring.data.search.SearchCriteria;
import io.commerce.spring.data.search.SearchOp;
import io.commerce.spring.data.search.SearchParser.AtomSearchContext;
import io.commerce.spring.data.search.SearchParser.CriteriaContext;
import io.commerce.spring.data.search.SearchParser.InputContext;
import io.commerce.spring.data.search.SearchParser.OpSearchContext;
import io.commerce.spring.data.search.SearchParser.PrioritySearchContext;
import org.springframework.data.jpa.domain.Specification;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.trimToEmpty;
import static org.apache.commons.lang3.StringUtils.trimToNull;

public class SearchVisitorImpl<T> extends SearchBaseVisitor<Specification<T>> {

    private static final String KEY_PATTERN = "(!?)(.*)";

    private static final Pattern keyPattern;

    static {
        keyPattern = Pattern.compile(KEY_PATTERN);
    }

    @Override
    public Specification<T> visitInput(InputContext ctx) {
        return super.visit(ctx != null ? ctx.search() : null);
    }

    @Override
    public Specification<T> visitOpSearch(OpSearchContext ctx) {

        List<Specification<T>> specificationList = new ArrayList<>();
        var left = visit(ctx != null ? ctx.left : null);
        var right = visit(ctx != null ? ctx.right : null);

        String logicalOp = ctx != null ? (ctx.logicalOp != null ? ctx.logicalOp.getText() : null) : null;

        switch (LogicalOp.logicalOp(logicalOp)) {
            case AND:
            case UNKNOWN:
                return and(left, right);
            case OR:
            default:
                return or(left, right);
        }
    }

    private Specification<T> and(Specification<T> left, Specification<T> right) {
        if (left == null) {
            return right;
        }
        return left.and(right);
    }

    private Specification<T> or(Specification<T> left, Specification<T> right) {
        if (left == null) {
            return right;
        }
        return left.or(right);
    }

    @Override
    public Specification<T> visitAtomSearch(AtomSearchContext ctx) {
        return super.visit(ctx != null ? ctx.criteria() : null);
    }

    @Override
    public Specification<T> visitPrioritySearch(PrioritySearchContext ctx) {
        return super.visit(ctx != null ? ctx.search() : null);
    }

    @Override
    public Specification<T> visitCriteria(CriteriaContext ctx) {
        String key = ctx.key().getText();
        String op = ctx.op() != null ? ctx.op().getText() : null;
        String value = ctx.value() != null
                ?
                trimToNull(ctx.value().getText()
                        .replaceAll("^\"|\"$", "")  // replace " if it's mot escaped
                        .replaceAll("^'|'$", "") // replace ' if it's not escaped
                        .replace("\\\"", "\"") // keep " if it's escaped
                        .replace("\\'", "'")) // keep ' if it's escaped
                :
                null;

        SearchOp searchOp = SearchOp.searchOp(trimToEmpty(op));
        Matcher matcher = keyPattern.matcher(URLDecoder.decode(trimToNull(key), StandardCharsets.UTF_8));
        if (matcher.matches()) {
            return buildSpecification(SearchCriteria.builder()
                    .exists(isEmpty(matcher.group(1)))
                    .key(matcher.group(2))
                    .op(searchOp)
                    .value(value).build());
        }
        return null;
    }

    private Specification<T> buildSpecification(SearchCriteria searchCriteria) {
        return SpecificationImpl.<T>builder()
                .searchCriteria(searchCriteria)
                .build();
    }
}
