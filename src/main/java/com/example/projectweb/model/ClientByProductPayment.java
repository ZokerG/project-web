package com.example.projectweb.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "CLIENT_BY_PRODUCT_PAYMENT")
public class ClientByProductPayment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long clientId;
    private long productId;
    private String paymentId;
    private int quantity;
    private double total;
}
