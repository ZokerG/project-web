package com.example.projectweb.repository;

import com.example.projectweb.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IProduct extends JpaRepository<Product, Long> {
    Optional<Product> findByName(String name);

    List<Product> findAllByCategoryId(long id);
}
