package ru.gb.rest.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.gb.rest.dao.CartRepository;
import ru.gb.rest.dao.ProductDao;
import ru.gb.rest.dto.ProductDto;
import ru.gb.rest.entity.Product;
import ru.gb.rest.entity.enums.Status;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductDao productDao;
    private final CartRepository cartRepository;


    public ProductDto save(ProductDto productDto) {
        Product savingProduct;
        Optional<Product> productFromDbOptional = Optional.empty();
        if (productDto.getId() != null) {
            productFromDbOptional = productDao.findById(productDto.getId());
        }
            savingProduct = productFromDbOptional.orElseGet(Product::new);
            savingProduct.setTitle(productDto.getTitle());
            savingProduct.setDate(productDto.getDate());
            savingProduct.setCost(productDto.getCost());
            savingProduct.setStatus(productDto.getStatus());
            savingProduct = productDao.save(savingProduct);
            productDto.setId(savingProduct.getId());

        return productDto;
    }

    @Transactional(readOnly = true)
    public Product findById(Long id) {
        return productDao.findById(id).orElse(null);
    }

    @Transactional(readOnly = true)
    public List<Product> findAll() {
        return productDao.findAll();
    }

    public void deleteById(Long id) {
        try {
            productDao.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            log.error("There isn't product with id {}", id);
        }

    }








    public void disableById(Long id) {
        productDao.findById(id).ifPresent(
                p -> {
                    p.setStatus(Status.DISABLE);
                    productDao.save(p);
                }
        );
    }

    public List<Product> findAllActive() {
        return productDao.findAllByStatus(Status.ACTIVE);
    }


    public List<Product> findAllActive(int page, int size) {
        return productDao.findAllByStatus(Status.ACTIVE, PageRequest.of(page, size));
    }

    public List<Product> findAllActiveSortedById(Sort.Direction direction) {
        return productDao.findAllByStatus(Status.ACTIVE, Sort.by(direction, "id"));
    }

    public List<Product> findAllActiveSortedById(int page, int size, Sort.Direction direction) {
        return productDao.findAllByStatus(Status.ACTIVE, PageRequest.of(page, size, Sort.by(direction, "id")));
    }

    @Transactional(propagation = Propagation.NEVER)
    public long count() {
        System.out.println(productDao.count());
        // какая-то логика
        return productDao.count();
    }

    public List<Product> showCart() {
        return cartRepository.showCart();
    }

    public void addProductToCart(Long id) {
        if (id != null) {
            Product productFromDb = productDao.findById(id).orElse(null);
            //if (productFromDb == null) throw new AssertionError();
            cartRepository.addProduct(productFromDb);
        }
    }

    public void deleteFromCartById(Long id) {
        List<Product> list = cartRepository.showCart();
        for (Product product : list) {
            if (product.getId().equals(id)) {
                cartRepository.deleteProduct(product);
                break;
            }
        }

    }
}
