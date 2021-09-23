package io.commerce.mongo.search;

public enum SearchOp {

    EQ, NE, GT, GE, LT, LE, EXISTS, UNKNOWN;

    public static SearchOp searchOp(String input) {
        switch (input) {
            case ":":
                return EQ;
            case "!:":
                return NE;
            case ">":
                return GT;
            case ">:":
                return GE;
            case "<":
                return LT;
            case "<:":
                return LE;
            default:
                return EXISTS;
        }
    }

}
