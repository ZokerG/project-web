package com.example.projectweb.service;


import com.example.projectweb.model.Category;
import com.example.projectweb.repository.ICategory;
import com.example.projectweb.request.CategoryRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    private final ICategory categoryRepository;

    public CategoryService(ICategory categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category create(CategoryRequest category) {

        if (categoryRepository.findByName(category.getName()).isPresent()) {
            throw new RuntimeException("Category already exists");
        }

        if (category.getName().isEmpty()) {
            throw new RuntimeException("Category name is empty");
        }

        Category newCategory = Category.builder().name(category.getName().toUpperCase()).description(category.getDescription()).build();
        return categoryRepository.save(newCategory);
    }

    public Category findById(long id) {
        return categoryRepository.findById(id).orElseThrow();
    }

    public Category findByName(String name) {
        return categoryRepository.findByName(name).orElseThrow();
    }

    public List<Category> findAllCategory() {
        return categoryRepository.findAll();
    }
}
