package com.ecommerce.shopping.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductDto {
    @NotNull
    private Long id;

    @Length(max = 255)
    @NotEmpty
    private String name;

    private String description;

    private Long quantity;

    private Float price;

    private  CategoryDto category;

    private  BrandDto brand;

    private  ColorDto color;
}
