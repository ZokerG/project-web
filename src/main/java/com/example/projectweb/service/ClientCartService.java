package com.example.projectweb.service;

import com.example.projectweb.model.ClientCart;
import com.example.projectweb.model.Product;
import com.example.projectweb.repository.IClientCart;
import com.example.projectweb.repository.IProduct;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ClientCartService {

    private final IClientCart clientCartRepository;
    private final IProduct productRepository;

    public ClientCartService(IClientCart clientCartRepository, IProduct productRepository) {
        this.clientCartRepository = clientCartRepository;
        this.productRepository = productRepository;
    }

    public void addCartByClient(long clientId, long productId, int quantity){
        ClientCart client = clientCartRepository.findByClientIdAndProductId(clientId, productId);
        if (client != null){
            clientCartRepository.delete(client);
        }
        ClientCart clientCart = new ClientCart();
        clientCart.setClientId(clientId);
        clientCart.setProductId(productId);
        clientCart.setQuantity(quantity);
        clientCartRepository.save(clientCart);
    }

    public List<Product> getCartByClient(long clientId){
        List<ClientCart> clientCart = clientCartRepository.findAllByClientId(clientId);

        if (clientCart.isEmpty()){
            return new ArrayList<>();
        }

        List<Product> product = clientCart.stream().map(clientCart1 ->
                productRepository.findById(clientCart1.getProductId()).orElseThrow(() -> new RuntimeException("Product not found")))
                .toList();
        return product;
    }

    public void deleteCartByClient(long clientId, long productId){
        ClientCart clientCart = clientCartRepository.findByClientIdAndProductId(clientId, productId);
        clientCartRepository.delete(clientCart);
    }
}
