package com.devsuperior.dscatalog.dto;

import com.devsuperior.dscatalog.entities.Product;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter @Setter
public class ProductMinDTO {

  private Long id;
  private String name;
  private double price;
  private String imgUrl;

  public ProductMinDTO(Long id, String name, double price, String imgUrl) {
    this.id = id;
    this.name = name;
    this.price = price;
    this.imgUrl = imgUrl;
  }

  public ProductMinDTO(Product product) {
    this.id = product.getId();
    this.name = product.getName();
    this.price = product.getPrice();
    this.imgUrl = product.getImgUrl();
  }



}
