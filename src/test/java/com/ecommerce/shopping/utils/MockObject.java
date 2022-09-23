package com.ecommerce.shopping.utils;

import com.ecommerce.shopping.entity.Brand;
import com.ecommerce.shopping.entity.Category;
import com.ecommerce.shopping.entity.Color;
import com.ecommerce.shopping.entity.Product;

import java.util.List;

public class MockObject {
    public static List<Product> mockProducts() {
        // Category
        Category category_1 = new Category(1l, "Dresses", false);
        Category category_2 = new Category(2l, "Furnishing", false);
        Category category_3 = new Category(3l, "Work cloths", false);
        Category category_4 = new Category(4l, "Active sportswear", false);
        // Brand
        Brand brand_1 = new Brand(1l, "Owen", false);
        Brand brand_2 = new Brand(2l, "Format", false);
        // Color
        Color color_1 = new Color(1l, "White", false);
        Color color_2 = new Color(2l, "Blue", false);
        Color color_3 = new Color(3l, "Pink", false);
        // Product
        Product product_1 = Product.builder()
                .id(1l)
                .name("Skinny High Ankle Jeans")
                .quantity(10L)
                .price(35.75f)
                .category(category_3)
                .brand(brand_1)
                .color(color_2)
                .isDeleted(false)
                .build();
        Product product_2 = Product.builder()
                .id(2l)
                .name("Vintage Straight High Jeans")
                .quantity(20L)
                .price(49.75f)
                .category(category_1)
                .brand(brand_2)
                .color(color_3)
                .isDeleted(false)
                .build();
        Product product_3 = Product.builder()
                .id(3l)
                .name("Halterneck bodycon dress")
                .quantity(15L)
                .price(40.95f)
                .category(category_1)
                .brand(brand_2)
                .color(color_3)
                .isDeleted(false)
                .build();
        Product product_4 = Product.builder()
                .id(4l)
                .name("Slip dress")
                .quantity(5L)
                .price(60.95f)
                .category(category_1)
                .brand(brand_2)
                .color(color_2)
                .isDeleted(false)
                .build();
        Product product_5 = Product.builder()
                .id(5l)
                .name("Satin wrap dress")
                .quantity(25L)
                .price(60.15f)
                .category(category_1)
                .brand(brand_1)
                .color(color_1)
                .isDeleted(false)
                .build();
        Product product_6 = Product.builder()
                .id(6l)
                .name("Patterned shirt dress")
                .quantity(12L)
                .price(38.15f)
                .category(category_1)
                .brand(brand_1)
                .color(color_2)
                .isDeleted(false)
                .build();
        Product product_7 = Product.builder()
                .id(7l)
                .name("Oversized shirt dress")
                .quantity(6L)
                .price(34.15f)
                .category(category_3)
                .brand(brand_1)
                .color(color_2)
                .isDeleted(false)
                .build();
        List<Product> products = List.of(product_1,
                product_2,
                product_3,
                product_4,
                product_5,
                product_6,
                product_7);

        return products;
    }
}
