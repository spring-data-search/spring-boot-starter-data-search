package app.commerceio.spring.data.search;

public enum SearchOp {

    EQ, NE, GT, GE, LT, LE, EXISTS;

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
