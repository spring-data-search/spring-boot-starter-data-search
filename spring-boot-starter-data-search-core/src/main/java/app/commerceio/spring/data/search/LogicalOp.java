package app.commerceio.spring.data.search;

import org.apache.commons.lang3.StringUtils;

public enum LogicalOp {

    AND, OR;

    public static LogicalOp logicalOp(String input) {
        switch (StringUtils.lowerCase(input)) {
            case "or":
                return OR;
            case "and":
            default:
                return AND;
        }
    }

}
