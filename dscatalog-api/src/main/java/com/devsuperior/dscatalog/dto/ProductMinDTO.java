package com.devsuperior.dscatalog.dto;

import com.devsuperior.dscatalog.entities.Product;

import java.util.Set;

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

  public ProductMinDTO(Product product, Set<CategoryDTO> categories) {
    this.id = product.getId();
    this.name = product.getName();
    this.price = product.getPrice();
    this.imgUrl = product.getImgUrl();
  }

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public double getPrice() {
    return price;
  }

  public void setPrice(double price) {
    this.price = price;
  }

  public String getImgUrl() {
    return imgUrl;
  }

  public void setImgUrl(String imgUrl) {
    this.imgUrl = imgUrl;
  }
}
