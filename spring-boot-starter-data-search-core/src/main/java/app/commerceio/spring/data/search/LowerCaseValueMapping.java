package app.commerceio.spring.data.search;

public class LowerCaseValueMapping implements ValueMapping {

    @Override
    public String map(String from) {
        return from == null ? null : from.toLowerCase();
    }
}
