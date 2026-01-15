package com.codebymathabo.controller;

import com.codebymathabo.common.ApiResponse;
import com.codebymathabo.service.VelocityService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

// Exposes endpoints for velocity calculations via REST.
@RestController
@RequestMapping("/api/velocity")
public class VelocityController {

    private final VelocityService velocityService;

    // Inject service to delegate business logic.
    public VelocityController(VelocityService velocityService) {
        this.velocityService = velocityService;
    }

    @PostMapping
    public ApiResponse<Map<String, Double>> calculateBatchVelocity(@RequestBody List<String> productIds) {
        try {
            // Validate input early to avoid processing invalid requests.
            if (productIds == null || productIds.isEmpty()) {
                return ApiResponse.error("Product ID list cannot be empty.");
            }

            // Delegate calculation to service to maintain separation of concerns.
            Map<String, Double> results = velocityService.calculateVelocityForBatch(productIds);

            // Wrap result in standard response format for consistent API contract.
            return ApiResponse.success(results);

        } catch (Exception e) {
            // Catch unexpected errors to prevent exposing stack traces to the client.
            System.err.println("Controller Error: " + e.getMessage());
            return ApiResponse.error("An internal error occurred while processing the request.");
        }
    }
}