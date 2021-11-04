package app.commerceio.spring.data.search.mongodb;

import app.commerceio.spring.data.search.LogicalOp;
import app.commerceio.spring.data.search.SearchBaseVisitor;
import app.commerceio.spring.data.search.SearchCriteria;
import app.commerceio.spring.data.search.SearchOp;
import app.commerceio.spring.data.search.SearchParser.AtomSearchContext;
import app.commerceio.spring.data.search.SearchParser.CriteriaContext;
import app.commerceio.spring.data.search.SearchParser.InputContext;
import app.commerceio.spring.data.search.SearchParser.OpSearchContext;
import app.commerceio.spring.data.search.SearchParser.PrioritySearchContext;
import org.springframework.data.mongodb.core.query.Criteria;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.trimToEmpty;
import static org.apache.commons.lang3.StringUtils.trimToNull;

public class SearchVisitorImpl extends SearchBaseVisitor<Criteria> {

    private static final String KEY_PATTERN = "(!?)(.*)";

    private static final Pattern keyPattern;

    static {
        keyPattern = Pattern.compile(KEY_PATTERN);
    }

    @Override
    public Criteria visitInput(InputContext ctx) {
        return super.visit(ctx != null ? ctx.search() : null);
    }

    @Override
    public Criteria visitOpSearch(OpSearchContext ctx) {

        List<Criteria> criteriaList = new ArrayList<>();
        var left = visit(ctx != null ? ctx.left : null);
        if (left != null) {
            criteriaList.add(left);
        }
        var right = visit(ctx != null ? ctx.right : null);
        if (right != null) {
            criteriaList.add(right);
        }

        String logicalOp = null;
        if (ctx != null) {
            logicalOp = ctx.logicalOp != null ? ctx.logicalOp.getText() : null;
        }

        switch (LogicalOp.logicalOp(logicalOp)) {
            case OR:
                return new Criteria().orOperator(criteriaList);
            case AND:
            case UNKNOWN:
            default:
                return new Criteria().andOperator(criteriaList);
        }
    }

    @Override
    public Criteria visitAtomSearch(AtomSearchContext ctx) {
        return super.visit(ctx != null ? ctx.criteria() : null);
    }

    @Override
    public Criteria visitPrioritySearch(PrioritySearchContext ctx) {
        return super.visit(ctx != null ? ctx.search() : null);
    }

    @Override
    public Criteria visitCriteria(CriteriaContext ctx) {
        String key = ctx.key().getText();
        String op = ctx.op() != null ? ctx.op().getText() : null;
        String value = ctx.value() != null ? ctx.value().getText() : null;

        SearchOp searchOp = SearchOp.searchOp(trimToEmpty(op));
        Matcher matcher = keyPattern.matcher(URLDecoder.decode(trimToNull(key), StandardCharsets.UTF_8));
        boolean matches = matcher.matches();
        return buildCriteria(SearchCriteria.builder()
                .exists(matches && isEmpty(matcher.group(1)))
                .key(matches ? matcher.group(2) : null)
                .op(searchOp)
                .value(value)
                .build());
    }

    private Criteria buildCriteria(SearchCriteria searchCriteria) {
        return CriteriaBuilder.builder()
                .searchCriteria(searchCriteria)
                .build()
                .toCriteria();
    }
}
