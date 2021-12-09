package app.commerceio.spring.data.search;

public class UpperCaseValueMapping implements ValueMapping {

    @Override
    public String map(String from) {
        return from == null ? null : from.toUpperCase();
    }
}
