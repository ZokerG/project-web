package com.example.projectweb.repository;

import com.example.projectweb.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ICategory extends JpaRepository<Category, Long> {
    Optional<Category> findByName(String name);
}
