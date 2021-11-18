package app.commerceio.spring.data.search.mongodb;

import app.commerceio.spring.data.search.SearchOp;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.text.NumberFormat;
import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
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

        private static final Pattern BOOLEAN_PATTERN;

        static {
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
                        .replaceAll("^\"|^'|\"$|'$", "")  // replace " or ' if it's not escaped
                        .replace("\\\"", "\"") // keep " if it's escaped
                        .replace("\\'", "'")); // keep ' if it's escaped

                String[] values = value.split("(?<!\\\\),");
                isArray = values.length > 1;
                if (isArray) {
                    if (Stream.of(values).allMatch(this::isDate)) {
                        type = Instant.class;
                    } else if (Stream.of(values).allMatch(this::isBoolean)) {
                        type = Boolean.class;
                    } else if (Stream.of(values).allMatch(this::isNumber)) {
                        type = Number.class;
                    }
                } else {
                    if (isDate(value)) {
                        type = Instant.class;
                    } else if (isBoolean(value)) {
                        type = Boolean.class;
                    } else if (isNumber(value)) {
                        type = Number.class;
                    }
                }
            }
            return new SearchCriteria(exists, key, op, value, isArray, type);
        }


        private boolean isNumber(String value) {
            if (value == null) {
                return false;
            }
            try {
                Number number = NumberFormat.getInstance().parse(value);
                return number != null;
            } catch (ParseException ignored) {
                return false;
            }
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
                Instant instant = OffsetDateTime.parse(value).toInstant();
                return instant != null;
            } catch (Exception retry) {
                try {
                    Instant instant = LocalDate.parse(value).atStartOfDay().atOffset(ZoneOffset.UTC).toInstant();
                    return instant != null;
                } catch (Exception ignored) {
                    return false;
                }
            }

        }
    }
}
