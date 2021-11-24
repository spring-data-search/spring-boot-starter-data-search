package app.commerceio.spring.data.search.jpa;

import app.commerceio.spring.data.search.LogicalOp;
import app.commerceio.spring.data.search.Mapper;
import app.commerceio.spring.data.search.SearchBaseVisitor;
import app.commerceio.spring.data.search.SearchOp;
import app.commerceio.spring.data.search.SearchParser.AtomSearchContext;
import app.commerceio.spring.data.search.SearchParser.CriteriaContext;
import app.commerceio.spring.data.search.SearchParser.InputContext;
import app.commerceio.spring.data.search.SearchParser.OpSearchContext;
import app.commerceio.spring.data.search.SearchParser.PrioritySearchContext;
import lombok.Builder;
import org.springframework.data.jpa.domain.Specification;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.trimToEmpty;
import static org.apache.commons.lang3.StringUtils.trimToNull;

@Builder
public class SearchVisitorImpl<T> extends SearchBaseVisitor<Specification<T>> {


    private static final String KEY_PATTERN = "(!?)(.*)";
    private static final Pattern keyPattern;

    static {
        keyPattern = Pattern.compile(KEY_PATTERN);
    }

    private final Mapper mapper;

    @Override
    public Specification<T> visitInput(InputContext ctx) {
        return super.visit(ctx != null ? ctx.search() : null);
    }

    @Override
    public Specification<T> visitOpSearch(OpSearchContext ctx) {

        var left = visit(ctx != null ? ctx.left : null);
        var right = visit(ctx != null ? ctx.right : null);

        String logicalOp = null;
        if (ctx != null) {
            logicalOp = ctx.logicalOp != null ? ctx.logicalOp.getText() : null;
        }

        switch (LogicalOp.logicalOp(logicalOp)) {
            case OR:
                return or(left, right);
            case AND:
            default:
                return and(left, right);
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
                        .replaceAll("^\"|^'|\"$|'$", "")  // replace " or ' if it's not escaped
                        .replace("\\\"", "\"") // keep " if it's escaped
                        .replace("\\'", "'")) // keep ' if it's escaped
                :
                null;

        SearchOp searchOp = SearchOp.searchOp(trimToEmpty(op));
        Matcher matcher = keyPattern.matcher(URLDecoder.decode(trimToNull(key), StandardCharsets.UTF_8));
        boolean matches = matcher.matches();
        String from = matches ? matcher.group(2) : null;
        String to = mapper != null ? mapper.map(from) : from;
        return SpecificationImpl.<T>builder()
                .exists(matches && isEmpty(matcher.group(1)))
                .key(to)
                .op(searchOp)
                .value(value).build();
    }
}
