package com.devsuperior.dscatalog.utils;

import com.devsuperior.dscatalog.entities.Product;
import com.devsuperior.dscatalog.projections.IdProjection;
import com.devsuperior.dscatalog.projections.ProductProjection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Utils {
  public static List<Product> replace(List<ProductProjection> ordered, List<Product> unOrdered) {
    Map<Long, Product> map = new HashMap<>();
    for (Product product : unOrdered) {
      map.putIfAbsent(product.getId(), product);
    }
    List<Product> result = new ArrayList<>();
    for (ProductProjection p : ordered) {
      result.add(map.get(p.getId()));
    }
    return result;
  }

  public static <ID> List<? extends IdProjection<ID>> replaceGeneric(
          List<? extends IdProjection<ID>> ordered, List<? extends IdProjection<ID>> unOrdered) {

    Map<ID, IdProjection<ID>> map = new HashMap<>();
    for (IdProjection<ID> product : unOrdered) {
      map.putIfAbsent(product.getId(), product);
    }
    List<IdProjection<ID>> result = new ArrayList<>();
    for (IdProjection<ID> p : ordered) {
      result.add(map.get(p.getId()));
    }
    return result;
  }

}
