package ru.gb.rest.dao;


import ru.gb.rest.entity.Product;

import java.util.List;


public interface CartRepository {

    List<Product> showCart();
    void deleteProduct(Product product);
    void addProduct(Product product);
}
