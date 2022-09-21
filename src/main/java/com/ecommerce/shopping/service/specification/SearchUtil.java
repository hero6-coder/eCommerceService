package com.ecommerce.shopping.service.specification;

import com.ecommerce.shopping.dto.specification.SearchCriteria;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class SearchUtil {
    public static <T> Specification<T> createSpec(List<SearchCriteria> queries) {
        SearchSpecificationBuilder<T> builder = new SearchSpecificationBuilder<>();
        for (SearchCriteria query : queries) {
            builder.with(query.getType().toString(), query.getKey(), query.getOperator(), query.getValue());
        }
        return builder.build();
    }
}
