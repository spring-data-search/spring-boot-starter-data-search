package io.commerce.spring.data.search.mongodb;

import io.commerce.spring.data.search.LogicalOp;
import io.commerce.spring.data.search.SearchBaseVisitor;
import io.commerce.spring.data.search.SearchCriteria;
import io.commerce.spring.data.search.SearchOp;
import org.apache.commons.lang3.StringUtils;
import org.bson.BsonRegularExpression;
import org.springframework.data.mongodb.core.query.Criteria;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.trimToEmpty;
import static org.apache.commons.lang3.StringUtils.trimToNull;

public class SearchVisitorImpl extends SearchBaseVisitor<Criteria> {

    private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    private static final String KEY_PATTERN = "(!?)(.*)";
    private static final String REGEX_WITH_OPTION_PATTERN = "^/(.*)/([imsx]*)$";
    private static final String REGEX_PATTERN = "^/(.*)$";
    private static final String NUMBER_PATTERN = "^(-?)(\\d+)([,.0-9]*)$";

    private static final Pattern keyPattern;
    private static final Pattern regExpWithOptionPattern;
    private static final Pattern regExpPattern;
    private static final Pattern numberPattern;

    static {
        keyPattern = Pattern.compile(KEY_PATTERN);
        regExpWithOptionPattern = Pattern.compile(REGEX_WITH_OPTION_PATTERN);
        regExpPattern = Pattern.compile(REGEX_PATTERN);
        numberPattern = Pattern.compile(NUMBER_PATTERN);
    }

    @Override
    public Criteria visitInput(io.commerce.spring.data.search.SearchParser.InputContext ctx) {
        return super.visit(ctx != null ? ctx.search() : null);
    }

    @Override
    public Criteria visitOpSearch(io.commerce.spring.data.search.SearchParser.OpSearchContext ctx) {

        List<Criteria> criteriaList = new ArrayList<>();
        var left = visit(ctx != null ? ctx.left : null);
        if (left != null) {
            criteriaList.add(left);
        }
        var right = visit(ctx != null ? ctx.right : null);
        if (right != null) {
            criteriaList.add(right);
        }

        String logicalOp = ctx != null ? (ctx.logicalOp != null ? ctx.logicalOp.getText() : null) : null;

        switch (LogicalOp.logicalOp(logicalOp)) {
            case AND:
            case UNKNOWN:
                return new Criteria().andOperator(criteriaList);
            case OR:
                return new Criteria().orOperator(criteriaList);
            default:
                throw new IllegalArgumentException();
        }
    }

    @Override
    public Criteria visitAtomSearch(io.commerce.spring.data.search.SearchParser.AtomSearchContext ctx) {
        return super.visit(ctx != null ? ctx.criteria() : null);
    }

    @Override
    public Criteria visitPrioritySearch(io.commerce.spring.data.search.SearchParser.PrioritySearchContext ctx) {
        return super.visit(ctx != null ? ctx.search() : null);
    }

    @Override
    public Criteria visitCriteria(io.commerce.spring.data.search.SearchParser.CriteriaContext ctx) {
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
            return buildCriteria(SearchCriteria.builder()
                    .exists(isEmpty(matcher.group(1)))
                    .key(matcher.group(2))
                    .op(searchOp)
                    .value(value).build());
        }
        return null;
    }

    private Criteria buildCriteria(SearchCriteria searchCriteria) {
        Object value = parseValue(searchCriteria.getValue());
        Criteria criteria = Criteria.where(searchCriteria.getKey());
        switch (searchCriteria.getOp()) {
            case EQ:
                return eq(value, criteria);
            case NE:
                return ne(value, criteria);
            case GT:
                return criteria.gt(value);
            case GE:
                return criteria.gte(value);
            case LT:
                return criteria.lt(value);
            case LE:
                return criteria.lte(value);
            case EXISTS:
                return criteria.exists(searchCriteria.isExists());
            case UNKNOWN:
                return null;
            default:
                throw new IllegalArgumentException();
        }
    }

    private Criteria eq(Object value, Criteria criteria) {
        if (value instanceof BsonRegularExpression) {
            criteria.regex((BsonRegularExpression) value);
        } else if (value instanceof List) {
            criteria.in(((List<?>) value).toArray());
        } else {
            criteria.is(value);
        }
        return criteria;
    }

    private Criteria ne(Object value, Criteria criteria) {
        if (value instanceof BsonRegularExpression) {
            criteria.not().regex((BsonRegularExpression) value);
        } else if (value instanceof List) {
            criteria.nin(((List<?>) value).toArray());
        } else {
            criteria.ne(value);
        }
        return criteria;
    }

    private Object parseValue(String value) {

        if (value == null || "null".equalsIgnoreCase(value)) {
            return null;
        }

        String[] parts = StringUtils.split(value, ",");
        if (parts.length > 1) {
            return Stream.of(parts)
                    .map(this::parseValue)
                    .collect(Collectors.toList());
        }

        if ("true".equalsIgnoreCase(value) || "false".equalsIgnoreCase(value)) {
            return Boolean.parseBoolean(value);
        }

        try {
            return parseLocalDateTime(value);
        } catch (DateTimeParseException | IllegalArgumentException ignored) {
        }

        try {
            if (numberPattern.matcher(value).matches()) {
                return NumberFormat.getInstance().parse(value);
            }
        } catch (ParseException ignored) {
        }

        Matcher matcherRegExpWithOption = regExpWithOptionPattern.matcher(value);
        if (matcherRegExpWithOption.matches()) {
            return new BsonRegularExpression(
                    matcherRegExpWithOption.group(1),
                    matcherRegExpWithOption.group(2));
        }

        Matcher matcherRegExp = regExpPattern.matcher(value);
        if (matcherRegExp.matches()) {
            return new BsonRegularExpression(
                    matcherRegExp.group(1));
        }

        return value;
    }

    private Instant parseLocalDateTime(String value) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
        return LocalDateTime.parse(value, formatter).atZone(ZoneOffset.UTC).toInstant();
    }
}
