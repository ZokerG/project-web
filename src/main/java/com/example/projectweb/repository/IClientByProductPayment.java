package com.example.projectweb.repository;

import com.example.projectweb.model.ClientByProductPayment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IClientByProductPayment extends JpaRepository<ClientByProductPayment, Long> {
    List<ClientByProductPayment> findAllByClientId(long clientId);
}
