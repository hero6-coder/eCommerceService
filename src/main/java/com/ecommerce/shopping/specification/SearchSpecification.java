package com.ecommerce.shopping.specification;

import com.ecommerce.shopping.dto.specification.SearchCriteria;
import com.ecommerce.shopping.entity.Brand;
import com.ecommerce.shopping.entity.Category;
import com.ecommerce.shopping.entity.Color;
import com.ecommerce.shopping.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;

import javax.persistence.criteria.*;
import java.math.BigDecimal;
import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SearchSpecification<T> implements Specification<T> {

    public static final String CATEGORY = "category";
    public static final String BRAND = "brand";
    public static final String COLOR = "color";

    private SearchCriteria criteria;

    @Override
    public Predicate toPredicate(@NonNull Root<T> root,@NonNull CriteriaQuery<?> criteriaQuery,@NonNull CriteriaBuilder criteriaBuilder) {
        switch (criteria.getKey()) {
            case CATEGORY: {
                Join<Product, Category> categoryJoin = root.join(CATEGORY);
                return criteriaBuilder.equal(categoryJoin.<String>get("name"), criteria.getValue());
            }
            case BRAND: {
                Join<Product, Brand> categoryJoin = root.join(BRAND);
                return criteriaBuilder.equal(categoryJoin.<String>get("name"), criteria.getValue());
            }
            case COLOR: {
                Join<Product, Color> categoryJoin = root.join(COLOR);
                return criteriaBuilder.equal(categoryJoin.<String>get("name"), criteria.getValue());
            }

        }
        // Product
        switch (criteria.getType()) {
            case NUMBER_PARAM: {
                if (criteria.getOperator().equals(">"))
                    return criteriaBuilder.gt(root.get(criteria.getKey()), new BigDecimal(criteria.getValue()));
                if (criteria.getOperator().equals("<"))
                    return criteriaBuilder.lt(root.get(criteria.getKey()), new BigDecimal(criteria.getValue()));
                if (criteria.getOperator().equals("="))
                    return criteriaBuilder.equal(root.get(criteria.getKey()), new BigDecimal(criteria.getValue()));
                if (criteria.getOperator().equals("!="))
                    return criteriaBuilder.notEqual(root.get(criteria.getKey()), new BigDecimal(criteria.getValue()));
                if (criteria.getOperator().equals(">="))
                    return criteriaBuilder.ge(root.get(criteria.getKey()), new BigDecimal(criteria.getValue()));
                if (criteria.getOperator().equals("<="))
                    return criteriaBuilder.le(root.get(criteria.getKey()), new BigDecimal(criteria.getValue()));
                if (criteria.getOperator().equals("in")) {
                    CriteriaBuilder.In<Object> in = criteriaBuilder.in(root.get(criteria.getKey()));
                    for (String str : criteria.getValue().split(",")) {
                        in.value(new BigDecimal(str));
                    }
                    return in;
                }
            }
            return null;
            case DATE_TIME_PARAM: {
                if (criteria.getOperator().equals(">"))
                    return criteriaBuilder.greaterThan(root.get(criteria.getKey()), Instant.parse(criteria.getValue()));
                if (criteria.getOperator().equals("<"))
                    return criteriaBuilder.lessThan(root.get(criteria.getKey()), Instant.parse(criteria.getValue()));
                if (criteria.getOperator().equals("="))
                    return criteriaBuilder.equal(root.get(criteria.getKey()), Instant.parse(criteria.getValue()));
                if (criteria.getOperator().equals("!="))
                    return criteriaBuilder.notEqual(root.get(criteria.getKey()), Instant.parse(criteria.getValue()));
                if (criteria.getOperator().equals(">="))
                    return criteriaBuilder.greaterThanOrEqualTo(root.get(criteria.getKey()), Instant.parse(criteria.getValue()));
                if (criteria.getOperator().equals("<="))
                    return criteriaBuilder.lessThanOrEqualTo(root.get(criteria.getKey()), Instant.parse(criteria.getValue()));
                if (criteria.getOperator().equals("in")) {
                    CriteriaBuilder.In<Object> in = criteriaBuilder.in(root.get(criteria.getKey()));
                    for (String str : criteria.getValue().split(","))
                        in.value(Instant.parse(str));
                    return in;
                }
            }
            return null;
            case STRING_PARAM: {
                if (criteria.getOperator().equals("="))
                    return criteriaBuilder.like(root.get(criteria.getKey()),
                        String.format("%%%s%%", criteria.getValue()));
                if (criteria.getOperator().equals("!="))
                    return criteriaBuilder.notLike(root.get(criteria.getKey()),
                            String.format("%%%s%%", criteria.getValue()));
                if (!criteria.getOperator().equals("in"))
                    return null;
                CriteriaBuilder.In<Object> in = criteriaBuilder.in(root.get(criteria.getKey()));
                for (String str : criteria.getValue().split(","))
                    in.value(str);
                return in;
            }
            case BOOLEAN_PARAM:
                return criteriaBuilder.equal(root.get(criteria.getKey()), Boolean.parseBoolean(criteria.getValue()));
            default:
                return null;
        }
    }
}
