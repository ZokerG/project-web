package com.example.projectweb.controller;

import com.example.projectweb.model.Product;
import com.example.projectweb.request.ProductRequest;
import com.example.projectweb.service.ProductService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public Product create(@RequestBody ProductRequest product) {
        return productService.create(product);
    }

    @GetMapping
    public List<Product> findAllProduct() {
        return productService.findAllProduct();
    }

    @GetMapping("/category")
    public List<Product> findAllProductByCategory(String name) {
        return productService.findAllProductByCategory(name);
    }

    @GetMapping("/{id}")
    public Product findById(@PathVariable long id) {
        return productService.findById(id);
    }
}
