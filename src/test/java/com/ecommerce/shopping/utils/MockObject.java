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
        Product product_1 = new Product(1L, "Skinny High Ankle Jeans", null,
                10L, 35.75f, category_3, brand_1, color_2, null, false);
        Product product_2 = new Product(2L, "Vintage Straight High Jeans", null,
                20L, 49.75f, category_1, brand_2, color_3, null, false);
        Product product_3 = new Product(3L, "Halterneck bodycon dress", null,
                15L, 40.95f, category_1, brand_2, color_3, null, false);
        Product product_4 = new Product(4L, "Slip dress", null,
                5L, 60.95f, category_1, brand_2, color_2, null, false);
        Product product_5 = new Product(5L, "Satin wrap dress", null,
                25L, 60.15f, category_1, brand_1, color_1, null, false);
        Product product_6 = new Product(6L, "Patterned shirt dress", null,
                12L, 38.15f, category_1, brand_1, color_2, null, false);
        Product product_7 = new Product(7L, "Oversized shirt dress", null,
                6L, 34.15f, category_3, brand_1, color_2, null, false);

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
