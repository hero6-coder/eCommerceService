package com.ecommerce.shopping.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    @NotNull
    private Long id;

    @Length(max = 255)
    @NotEmpty
    @Pattern(regexp = "^[a-zA-Z0-9_.-]*$")
    private String name;

    private String description;

    private Long quantity;

    private Float price;

    private  CategoryDto category;

    private  BrandDto brand;

    private  ColorDto color;
}
