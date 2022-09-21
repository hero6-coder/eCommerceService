package com.ecommerce.shopping.dto.specification;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchCriteria {
    private SearchParamTypeEnum type;
    private String key;
    private String operator;
    private String value;

    public SearchCriteria(String type, String key, String operator, String value) {
        this.type = SearchParamTypeEnum.valueOf(type);
        this.key = key;
        this.operator = operator;
        this.value = value;
    }
}
