package app.commerceio.spring.data.search.jpa;

import app.commerceio.spring.data.search.SearchOp;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.criteria.From;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.apache.commons.lang3.StringUtils.trimToNull;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SearchCriteria {
    private boolean exists;
    private String key;
    private SearchOp op;
    private String value;
    private Path<?> path;
    private boolean isArray;
    private Class<?> type;
    private boolean startsWith;
    private boolean endsWith;

    public static SearchCriteriaBuilder builder() {
        return new SearchCriteriaBuilder();
    }

    public static class SearchCriteriaBuilder {

        private boolean exists;
        private String key;
        private SearchOp op;
        private String value;
        private Root<?> root;

        private static final String VALUE_PATTERN = "((?<!\\\\)\\*?)(.+?)((?<!\\\\)\\*?)$";
        private static final Pattern valuePattern;

        static {
            valuePattern = Pattern.compile(VALUE_PATTERN);
        }

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

        public SearchCriteriaBuilder root(Root<?> root) {
            this.root = root;
            return this;
        }

        public SearchCriteria build() {
            boolean isArray = false;
            boolean startsWith = false;
            boolean endsWith = false;
            String key = getKey(this.key);
            Path<?> path = getPath(root, this.key);
            Class<?> type = path != null ? path.get(key).getJavaType() : String.class;
            if (value != null) {
                value = StringUtils.trimToNull(value
                        .replaceAll("^\"|^'|\"$|'$", "")  // replace " or ' if it's not escaped
                        .replace("\\\"", "\"") // keep " if it's escaped
                        .replace("\\'", "'")); // keep ' if it's escaped

                Matcher matcher = valuePattern.matcher(URLDecoder.decode(trimToNull(value), StandardCharsets.UTF_8));
                if (matcher.matches()) {
                    endsWith = "*".equals(matcher.group(1));
                    value = matcher.group(2);
                    startsWith = "*".equals(matcher.group(3));
                }

                String[] values = value.split("(?<!\\\\),");
                isArray = values.length > 1;
            }
            return new SearchCriteria(exists, key, op, value, path, isArray, type, startsWith, endsWith);
        }

        private Path<?> getPath(Root<?> root, String key) {
            if (StringUtils.isBlank(key)) {
                return root;
            }
            String[] keyArray = StringUtils.split(key, ".");
            if (keyArray.length <= 1) {
                return root;
            }
            String[] keyList = Arrays.copyOf(keyArray, keyArray.length - 1);
            Path<?> path = root;
            for (String k : keyList) {
                path = getPath(path, k);
            }
            return path;
        }

        private Path<?> getPath(Path<?> path, String k) {
            if (Collection.class.isAssignableFrom(path.get(k).getJavaType())) {
                From<?, ?> current = (From<?, ?>) path;
                return current.join(k, JoinType.LEFT);
            } else {
                return path.get(k);
            }
        }

        private String getKey(String key) {
            List<String> keys = Arrays.asList(StringUtils.split(key, "."));
            return keys.get(keys.size() - 1);
        }
    }
}
