package ru.gb.rest.entity;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.gb.rest.dao.CartRepository;

import java.util.ArrayList;
import java.util.List;


@Component
@RequiredArgsConstructor
@Getter
public class Cart implements CartRepository {


    private List<Product> products = new ArrayList<>();

    public List<Product> showCart() {
        return products;
    }

    @Override
    public void deleteProduct(Product product) {
        products.remove(product);
    }

    @Override
    public void addProduct(Product product) {
        products.add(product);
    }

}
