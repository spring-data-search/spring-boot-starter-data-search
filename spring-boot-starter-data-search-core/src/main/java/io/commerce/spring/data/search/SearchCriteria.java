package io.commerce.spring.data.search;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.regex.Pattern;
import java.util.stream.Stream;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SearchCriteria {
    private boolean exists;
    private String key;
    private SearchOp op;
    private String value;
    private boolean isArray;
    private Class<?> type;

    public static SearchCriteriaBuilder builder() {
        return new SearchCriteriaBuilder();
    }

    public static class SearchCriteriaBuilder {

        private static final Pattern NUMBER_PATTERN;
        private static final Pattern BOOLEAN_PATTERN;
        public static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

        static {
            NUMBER_PATTERN = Pattern.compile("^(-?)(\\d+)([,.0-9]*)$");
            BOOLEAN_PATTERN = Pattern.compile("^(false|true)$", Pattern.CASE_INSENSITIVE);
        }

        private boolean exists;
        private String key;
        private SearchOp op;
        private String value;

        SearchCriteriaBuilder() {
        }

        public SearchCriteriaBuilder exists(boolean exists) {
            this.exists = exists;
            return this;
        }

        public SearchCriteriaBuilder key(String key) {
            this.key = key;
            return this;
        }

        public SearchCriteriaBuilder op(SearchOp op) {
            this.op = op;
            return this;
        }

        public SearchCriteriaBuilder value(String value) {
            this.value = value;
            return this;
        }

        public SearchCriteria build() {
            boolean isArray = false;
            Class<?> type = String.class;
            if (value != null) {
                value = StringUtils.trimToNull(value
                        .replaceAll("^\"|\"$", "")  // replace " if it's not escaped
                        .replaceAll("^'|'$", "") // replace ' if it's not escaped
                        .replace("\\\"", "\"") // keep " if it's escaped
                        .replace("\\'", "'")); // keep ' if it's escaped

                String[] values = value.split("(?<!\\\\),");
                isArray = values.length > 1;
                if (isArray) {
                    if (Stream.of(values).allMatch(this::isNumber)) {
                        type = Number.class;
                    } else if (Stream.of(values).allMatch(this::isBoolean)) {
                        type = Boolean.class;
                    } else if (Stream.of(values).allMatch(this::isDate)) {
                        type = Instant.class;
                    }
                } else {
                    if (isNumber(value)) {
                        type = Number.class;
                    } else if (isBoolean(value)) {
                        type = Boolean.class;
                    } else if (isDate(value)) {
                        type = Instant.class;
                    }
                }
            }
            return new SearchCriteria(exists, key, op, value, isArray, type);
        }


        private boolean isNumber(String value) {
            if (value == null) {
                return false;
            }
            return NUMBER_PATTERN.matcher(value).matches();
        }

        private boolean isBoolean(String value) {
            if (value == null) {
                return false;
            }
            return BOOLEAN_PATTERN.matcher(value).matches();
        }

        private boolean isDate(String value) {
            if (value == null) {
                return false;
            }
            try {
                OffsetDateTime.parse(value).toInstant();
            } catch (Exception ignored) {
                return false;
            }
            return true;
        }
    }
}
