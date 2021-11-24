package app.commerceio.spring.data.search.jpa;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum LikePattern {
    STARTS_WITH("{0}%"), ENDS_WITH("%{0}"), CONTAINS("%{0}%"), UNKNOWN("{0}");

    private final String pattern;

    public static LikePattern pattern(boolean startsWith, boolean endsWith) {
        if (startsWith && endsWith) {
            return CONTAINS;
        } else if (startsWith) {
            return STARTS_WITH;
        } else if (endsWith) {
            return ENDS_WITH;
        } else {
            return UNKNOWN;
        }
    }
}
