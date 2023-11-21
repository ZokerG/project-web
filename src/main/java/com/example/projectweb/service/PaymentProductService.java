package com.example.projectweb.service;

import com.example.projectweb.model.Client;
import com.example.projectweb.model.ClientByProductPayment;
import com.example.projectweb.model.ClientCart;
import com.example.projectweb.model.Product;
import com.example.projectweb.repository.IClient;
import com.example.projectweb.repository.IClientByProductPayment;
import com.example.projectweb.repository.IClientCart;
import com.example.projectweb.repository.IProduct;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.model.checkout.Session;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.IntStream;

@Service
public class PaymentProductService {

    private final IProduct productRepository;
    private final IClient clientRepository;
    private final IClientByProductPayment clientByProductPaymentRepository;
    private final IClientCart clientCartRepository;

    @Value("${STRIPE_SECRET_KEY}")
    private String STRIPE_SECRET_KEY;

    public PaymentProductService(IProduct productRepository, IClient clientRepository, IClientByProductPayment clientByProductPaymentRepository, IClientCart clientCartRepository) {
        this.productRepository = productRepository;
        this.clientRepository = clientRepository;
        this.clientByProductPaymentRepository = clientByProductPaymentRepository;
        this.clientCartRepository = clientCartRepository;
    }

    public String paymentProduct(long clientId, long productId, int quantity) {
        try {
            Stripe.apiKey = STRIPE_SECRET_KEY;

            Optional<Client> client = clientRepository.findById(clientId);
            Product product = productRepository.findById(productId).orElseThrow(
                    () -> new RuntimeException("Product not found")
            );

            double total = product.getPrice() * quantity;

            Map<String, Object> lineItem = new HashMap<>();
            lineItem.put("price_data", new HashMap<String, Object>() {{
                put("currency", "usd");
                put("product_data", new HashMap<String, Object>() {{
                    put("name", product.getName());
                }});
                put("unit_amount", (long) (product.getPrice() * 100));
            }});
            lineItem.put("quantity", (long) quantity);

            Map<String, Object> params = new HashMap<>();
            params.put("payment_method_types", Arrays.asList("card"));
            params.put("mode", "payment");
            params.put("line_items", Arrays.asList(lineItem));
            params.put("success_url", "http://localhost:4200/pages/home");
            params.put("cancel_url", "http://localhost:4200/pages/home");

            Session session = Session.create(params);
            String stripeCheckoutUrl = session.getUrl();
            ClientByProductPayment clientByProductPayment = new ClientByProductPayment();
            clientByProductPayment.setClientId(clientId);
            clientByProductPayment.setProductId(productId);
            clientByProductPayment.setPaymentId(session.getId());
            clientByProductPayment.setQuantity(quantity);
            clientByProductPayment.setTotal(total);
            clientByProductPaymentRepository.save(clientByProductPayment);
            return session.getUrl();
        } catch (StripeException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<PaymentIntent> getPaymentList() {
        try {
            Stripe.apiKey = STRIPE_SECRET_KEY;

            Map<String, Object> params = new HashMap<>();
            params.put("limit", 10);

            List<PaymentIntent> paymentIntents = PaymentIntent.list(params).getData();

            return paymentIntents;

        } catch (StripeException e) {
            // Manejar excepciones según tus necesidades
            e.printStackTrace();
            return null;
        }
    }

    public String paymentCart(long clientId, List<Long> products) {
        List<Product> productList = products.stream().map(
                productRepository::findById).map(Optional::get).toList();

        double total = productList.stream().mapToDouble(Product::getPrice).sum();

        try {
            Stripe.apiKey = STRIPE_SECRET_KEY;

            Map<String, Object> lineItem = new HashMap<>();
            lineItem.put("price_data", new HashMap<String, Object>() {{
                put("currency", "usd");
                put("product_data", new HashMap<String, Object>() {{
                    put("name", "Carrito de compras");
                }});
                put("unit_amount", (long) (total * 100));
            }});
            lineItem.put("quantity", (long) 1);

            Map<String, Object> params = new HashMap<>();
            params.put("payment_method_types", Arrays.asList("card"));
            params.put("mode", "payment"); // Agrega el modo de la sesión
            params.put("line_items", Arrays.asList(lineItem));
            params.put("success_url", "http://localhost:4200/pages/home");
            params.put("cancel_url", "http://localhost:4200/pages/home");

            Session session = Session.create(params);
            String sessionId = session.getId();
            IntStream.range(0, productList.size())
                    .forEach(i -> {
                        Product product = productList.get(i);
                        ClientByProductPayment clientByProductPayment = new ClientByProductPayment();
                        clientByProductPayment.setClientId(clientId);
                        clientByProductPayment.setProductId(product.getId());
                        clientByProductPayment.setPaymentId(sessionId);
                        clientByProductPayment.setQuantity(i + 1);
                        clientByProductPayment.setTotal(product.getPrice());
                        clientByProductPaymentRepository.save(clientByProductPayment);
                        deleteCartByClient(clientId, product.getId());
                    });
            return session.getUrl();
        } catch (StripeException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Product> getProductsByClient(long clientId) {
        List<ClientByProductPayment> clientByProductPaymentList = clientByProductPaymentRepository.findAllByClientId(clientId);
        List<Product> productList = clientByProductPaymentList.stream().map(clientByProductPayment ->
                productRepository.findById(clientByProductPayment.getProductId()).orElseThrow(() -> new RuntimeException("Product not found")))
                .toList();
        return productList;
    }


    public void deleteCartByClient(long clientId, long productId){
        ClientCart clientByProductPayment = clientCartRepository.findByClientIdAndProductId(clientId, productId);
        clientCartRepository.delete(clientByProductPayment);
    }
}
