package io.commerce.mongo.search;

import org.apache.commons.lang3.StringUtils;

public enum LogicalOp {

    AND, OR, UNKNOWN;

    public static LogicalOp logicalOp(String input) {
        switch (StringUtils.lowerCase(input)) {
            case "and":
                return AND;
            case "or":
                return OR;
            default:
                return UNKNOWN;
        }
    }

}
