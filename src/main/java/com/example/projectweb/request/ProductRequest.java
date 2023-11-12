package com.example.projectweb.request;

import lombok.Data;

@Data
public class ProductRequest {
    private long clientId;
    private long categoryId;
    private String name;
    private String description;
    private double price;
    private String imageUrl;
}
