package com.codebymathabo.repository;

import com.codebymathabo.model.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

// usage: Handles all talk with the database so the Service doesn't have to.
@Repository
public interface SalesRepository extends JpaRepository<Sale, Long> {

    // usage: Used to get the "last 7 days" of sales for a batch of items.
    List<Sale> findByProductIdInAndSaleDateAfter(List<String> productIds, LocalDate startDate);

    // Fetch all sales for a single product.
    List<Sale> findByProductId(String productId);
}