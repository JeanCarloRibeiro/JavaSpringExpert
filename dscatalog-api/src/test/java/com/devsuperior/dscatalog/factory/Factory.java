package com.devsuperior.dscatalog.factory;

import com.devsuperior.dscatalog.dto.CategoryDTO;
import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.dto.ProductMinDTO;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.entities.Product;

import java.time.Instant;

public class Factory {

  public static Product createProduct() {
    Product product = new Product(1L, "Phone", "Good Phone", 800.0, "https://img.com/img.png", Instant.parse("2020-10-20T03:00:00Z"));
    product.getCategories().add(new Category(2L, "Eletronics"));
    return product;
  }

  public static ProductDTO createProductDTO() {
    Product product = createProduct();
    return new ProductDTO(product, product.getCategories());
  }

  public static ProductMinDTO createProductMinDTO() {
    Product product = createProduct();
    return new ProductMinDTO(product);
  }

  public static Category createCategory() {
    return new Category(2L, "Eletronics");
  }

  public static CategoryDTO createCategoryDTO() {
    Category category = createCategory();
    return new CategoryDTO(category);
  }

}
