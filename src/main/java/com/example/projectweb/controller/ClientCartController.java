package com.example.projectweb.controller;

import com.example.projectweb.model.Product;
import com.example.projectweb.service.ClientCartService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/client/cart")
public class ClientCartController {

    private final ClientCartService clientCartService;

    public ClientCartController(ClientCartService clientCartService) {
        this.clientCartService = clientCartService;
    }

    @GetMapping("/{clientId}")
    public List<Product> getCartByClient(@PathVariable long clientId){
        return clientCartService.getCartByClient(clientId);
    }

    @PostMapping("/add/{clientId}/{productId}")
    public void addCartByClient(@PathVariable long clientId, @PathVariable long productId, @RequestParam int quantity){
        clientCartService.addCartByClient(clientId, productId, quantity);
    }

    @DeleteMapping("/delete/{clientId}/{productId}")
    public void deleteCartByClient(@PathVariable long clientId, @PathVariable long productId){
        clientCartService.deleteCartByClient(clientId, productId);
    }
}
