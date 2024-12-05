package com.devsuperior.dscatalog.repositories;

import com.devsuperior.dscatalog.projections.ProductProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.devsuperior.dscatalog.entities.Product;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

  @Query(value =
          "SELECT obj FROM Product obj JOIN FETCH obj.categories " +
                  "WHERE (UPPER(obj.name) LIKE UPPER(CONCAT('%', :name, '%')) OR :name IS NULL)",
          countQuery =
                  "SELECT count(obj) FROM Product obj JOIN obj.categories " +
                          "WHERE (UPPER(obj.name) LIKE UPPER(CONCAT('%', :name, '%')) OR :name IS NULL)")
  Page<Product> searchByNamePageable(String name, Pageable pageable);

  @Query(nativeQuery = true,
          value = """
            SELECT * FROM (
              SELECT DISTINCT tb_product.id, tb_product.name
              FROM tb_product
              INNER JOIN tb_product_category ON tb_product_category.product_id = tb_product.id 
              WHERE (tb_product_category.category_id IN :categoryIds OR :categoryIds IS NULL) 
              AND (UPPER(tb_product.name) LIKE UPPER(CONCAT('%', :name, '%')) OR :name IS NULL) 
            ) as tb_result
         """, countQuery = """
              SELECT COUNT(*) FROM (
                    SELECT DISTINCT tb_product.id, tb_product.name
                    FROM tb_product
                    INNER JOIN tb_product_category ON tb_product_category.product_id = tb_product.id
                    WHERE (tb_product_category.category_id IN :categoryIds OR :categoryIds IS NULL)
                    AND (UPPER(tb_product.name) LIKE UPPER(CONCAT('%', :name, '%')) OR :name IS NULL)
                  ) AS tb_result
          """)
  Page<ProductProjection> searchProducts(String name, List<Long> categoryIds, Pageable pageable);

  @Query(value = "SELECT obj FROM Product obj JOIN FETCH obj.categories WHERE obj.id IN :productIds ORDER BY obj.name")
  List<Product> searchProductsWithCategories(List<Long> productIds);

}
