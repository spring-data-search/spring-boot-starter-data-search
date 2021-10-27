package io.commerce.spring.data.search;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchCriteria {
    private boolean exists;
    private String key;
    private SearchOp op;
    private String value;
}
