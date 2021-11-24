package app.commerceio.spring.data.search.jpa.parser;

import java.text.NumberFormat;
import java.text.ParseException;

public class NumberPredicateBuilder implements PredicateBuilder<Number> {

    @Override
    public Number parse(String value) {
        try {
            return NumberFormat.getInstance().parse(value);
        } catch (ParseException exception) {
            throw new IllegalArgumentException(exception);
        }
    }
}
