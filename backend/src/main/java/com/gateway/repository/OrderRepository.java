package com.gateway.repository;

import com.gateway.model.Order;
import com.gateway.model.Merchant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, String> {

    Optional<Order> findByIdAndMerchant(String id, Merchant merchant);

    List<Order> findByMerchant(Merchant merchant);
}
