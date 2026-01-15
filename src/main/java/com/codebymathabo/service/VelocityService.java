package com.codebymathabo.service;

import com.codebymathabo.model.Sale;
import com.codebymathabo.repository.SalesRepository;
import com.codebymathabo.strategy.VelocityStrategy;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class VelocityService {

    private final SalesRepository salesRepository;
    private final VelocityStrategy velocityStrategy;

    // Constructor injection ensures dependencies are immutable and testable.
    public VelocityService(SalesRepository salesRepository, VelocityStrategy velocityStrategy) {
        this.salesRepository = salesRepository;
        this.velocityStrategy = velocityStrategy;
    }

    public Map<String, Double> calculateVelocityForBatch(List<String> productIds) {
        // Fail fast if the input list is empty to save processing resources.
        if (productIds == null || productIds.isEmpty()) {
            System.out.println("Warning: Received empty product list. Skipping processing.");
            return Collections.emptyMap();
        }

        // Analyzes the last 7 days to determine the weekly sales trend.
        LocalDate sevenDaysAgo = LocalDate.now().minusDays(7);

        try {
            // Fetch all data in one query to avoid calling DataBase for every product.
            List<Sale> sales = salesRepository.findByProductIdInAndSaleDateAfter(productIds, sevenDaysAgo);

            // Group sales by ID using a Hash Map for O(1) lookup speed during the loop.
            Map<String, List<Sale>> salesByProduct = sales.stream()
                    .collect(Collectors.groupingBy(Sale::getProductId));

            Map<String, Double> velocityResults = new HashMap<>();

            for (String productId : productIds) {
                // Retrieve sales for the current product or use an empty list if none exist.
                List<Sale> productSales = salesByProduct.getOrDefault(productId, Collections.emptyList());

                // Calculate total quantity sold for this specific product.
                long totalUnitsSold = productSales.stream().mapToInt(Sale::getQuantity).sum();

                // Delegate the math to the strategy to enabling swaping formulas later.
                double velocity = velocityStrategy.calculate(totalUnitsSold, 7);

                velocityResults.put(productId, velocity);

                // Log the outcome to trace which strategy was used for specific items.
                System.out.println("Product: " + productId + " | Velocity: " + velocity + " | Strategy: " + velocityStrategy.getStrategyName());
            }

            return velocityResults;

        } catch (Exception e) {
            // Rethrow exceptions so the global error handler can manage the HTTP response.
            System.err.println("Error calculating velocity: " + e.getMessage());
            throw new RuntimeException("Failed to calculate velocity", e);
        }
    }
}