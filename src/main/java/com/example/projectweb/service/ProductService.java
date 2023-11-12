package com.example.projectweb.service;

import com.example.projectweb.model.Category;
import com.example.projectweb.model.Product;
import com.example.projectweb.repository.IProduct;
import com.example.projectweb.request.ProductRequest;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
public class ProductService {

    private final IProduct productRepository;
    private final CategoryService categoryService;

    public ProductService(IProduct productRepository, CategoryService categoryService) {
        this.productRepository = productRepository;
        this.categoryService = categoryService;
    }

    public Product create(@Validated ProductRequest product) {

        if (productRepository.findByName(product.getName()).isPresent()) {
            throw new RuntimeException("Product already exists");
        }

        if (product.getName().isEmpty()) {
            throw new RuntimeException("Product name is empty");
        }

        Product newProduct = Product.builder().name(product.getName()).description(product.getDescription())
                .clientId(product.getClientId())
                .categoryId(product.getCategoryId())
                .price(product.getPrice())
                .imageUrl(product.getImageUrl()).build();
        return productRepository.save(newProduct);
    }

    public Product findById(long id) {
        return productRepository.findById(id).orElseThrow();
    }

    public List<Product> findAllProduct() {
        return productRepository.findAll();
    }

    public List<Product> findAllProductByCategory(String name) {
        String nameCategory = name.toUpperCase();
        Category category = categoryService.findByName(nameCategory);
        return productRepository.findAllByCategoryId(category.getId());
    }
}
