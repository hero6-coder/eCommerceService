package com.ecommerce.shopping.mapper;

import com.ecommerce.shopping.dto.BrandDto;
import com.ecommerce.shopping.dto.CategoryDto;
import com.ecommerce.shopping.dto.ColorDto;
import com.ecommerce.shopping.dto.ProductDto;
import com.ecommerce.shopping.entity.Brand;
import com.ecommerce.shopping.entity.Category;
import com.ecommerce.shopping.entity.Color;
import com.ecommerce.shopping.entity.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    Product toEntity(ProductDto dto);

    ProductDto toDto(Product entity);

    CategoryDto categoryToCategoryDto(Category entity);

    Category categoryDtoToCategory(CategoryDto dto);

    BrandDto brandToBrandDto(Brand entity);

    Brand brandDtoToBrand(BrandDto dto);

    ColorDto colorToColorDto(Color entity);

    Color colorDtoToCategory(ColorDto dto);

}
