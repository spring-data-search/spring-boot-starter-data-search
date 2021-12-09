package app.commerceio.spring.data.search;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class NumberToTextValueMapping implements ValueMapping {

    @Override
    public String map(String from) {
        switch (from) {
            case "1":
                return "one";
            case "2":
                return "two";
            default:
                return "zero";
        }
    }
}
