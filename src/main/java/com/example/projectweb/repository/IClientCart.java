package com.example.projectweb.repository;

import com.example.projectweb.model.ClientCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IClientCart extends JpaRepository<ClientCart, Long> {
    List<ClientCart> findAllByClientId(long clientId);

    ClientCart findByClientIdAndProductId(long clientId, long productId);
}
