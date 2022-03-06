package ru.gb.rest.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.gb.rest.entity.Product;
import ru.gb.rest.service.ProductService;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/cart")
public class CartController {

    private final ProductService productService;


    @GetMapping
    public List<Product> showCart () {
        return productService.showCart();
    }

    @GetMapping("/add/{productId}")
    public ResponseEntity<?> addToCart(@PathVariable("productId") Long id) {
        if (id != null) {
            productService.addProductToCart(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    @DeleteMapping("/delete/{productId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFromCart(@PathVariable("productId") Long id) {
        productService.deleteFromCartById(id);
    }
}
