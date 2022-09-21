package com.ecommerce.shopping.service.specification;

import com.ecommerce.shopping.dto.specification.SearchCriteria;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Data
@AllArgsConstructor
public class SearchSpecificationBuilder<T> {
    private final List<SearchCriteria> criteriaList;

    public SearchSpecificationBuilder() {
        criteriaList = new ArrayList<>();
    }

    public SearchSpecificationBuilder<T> with (String type, String key, String operator, String value) {
        criteriaList.add(new SearchCriteria(type, key, operator, value));
        return this;
    }

    public Specification<T> build() {
        if (criteriaList.isEmpty()) {
            return null;
        }
        Iterator<SearchCriteria> iterator = criteriaList.iterator();
        SearchCriteria criteria = iterator.next();
        Specification<T> result = Specification.where(new SearchSpecification<>(criteria));
        while (iterator.hasNext()) {
            criteria = iterator.next();
            result = result.and(new SearchSpecification<>(criteria));
        }
        return result;
    }
}
