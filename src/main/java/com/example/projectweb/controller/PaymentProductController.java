package com.example.projectweb.controller;

import com.example.projectweb.model.Product;
import com.example.projectweb.service.PaymentProductService;
import com.stripe.model.PaymentIntent;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payment")
public class PaymentProductController {

    private final PaymentProductService paymentProductService;

    public PaymentProductController(PaymentProductService paymentProductService) {
        this.paymentProductService = paymentProductService;
    }

    @GetMapping("/list")
    public List<PaymentIntent> getPayments() {
        return paymentProductService.getPaymentList();
    }

    @PostMapping("/product/{clientId}/{productId}/{quantity}")
    public String paymentProduct(@PathVariable long clientId,@PathVariable long productId,@PathVariable int quantity) {
        return paymentProductService.paymentProduct(clientId, productId, quantity);
    }

    @PostMapping("/cart/{clientId}")
    public String paymentCart(@PathVariable long clientId, @RequestBody List<Long> productsId){
        return paymentProductService.paymentCart(clientId, productsId);
    }

    @GetMapping("/product/{id}")
    public List<Product> getProductsByPayment(@PathVariable long id){
        return paymentProductService.getProductsByClient(id);
    }
}
