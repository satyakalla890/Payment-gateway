package com.gateway.repository;

import com.gateway.model.Payment;
import com.gateway.model.Merchant;
import com.gateway.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, String> {

    Optional<Payment> findById(String id);

    Optional<Payment> findByIdAndMerchant(String id, Merchant merchant);

    List<Payment> findByOrder(Order order);

    List<Payment> findByStatus(String status);
}
