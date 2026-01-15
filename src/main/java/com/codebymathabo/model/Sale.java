package com.codebymathabo.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "sales")
public class Sale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Indexed column because sales will be frequently searched by their Product ID.
    @Column(nullable = false, name = "product_id")
    private String productId;

    @Column(nullable = false)
    private int quantity;

    // Used LocalDate instead of LocalDateTime because the business logic measures velocity in 'days'.
    @Column(nullable = false, name = "sale_date")
    private LocalDate saleDate;

    // Default constructor for fetching data.
    public Sale() {
    }

    public Sale(String productId, int quantity, LocalDate saleDate) {
        this.productId = productId;
        this.quantity = quantity;
        this.saleDate = saleDate;
    }

    public Long getId() {
        return id;
    }

    public String getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public LocalDate getSaleDate() {
        return saleDate;
    }

    // Setters are omitted for 'id' to protect the database primary key from being changed in code.

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setSaleDate(LocalDate saleDate) {
        this.saleDate = saleDate;
    }
}