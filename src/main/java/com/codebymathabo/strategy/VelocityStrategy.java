package com.codebymathabo.strategy;

/**
 * usage: Defines a contract for calculating sales velocity, allowing the algorithm
 * to be swapped at runtime (Strategy Pattern).
 */
public interface VelocityStrategy {

    // Calculates the velocity score based on sales volume and time period.
    double calculate(long totalUnitsSold, int durationInDays);

    // Returns the name of the strategy for logging and debugging purposes.
    String getStrategyName();
}