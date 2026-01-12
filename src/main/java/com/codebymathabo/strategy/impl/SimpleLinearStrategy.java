package com.codebymathabo.strategy.impl;

import com.codebymathabo.strategy.VelocityStrategy;
import org.springframework.stereotype.Component;

/**
 * Standard strategy: calculates average sales per day.
 * Formula: Velocity = Total Units Sold / Total Days.
*/
@Component("simpleLinearStrategy")
public class SimpleLinearStrategy implements VelocityStrategy {

    @Override
    public double calculate(long totalUnitsSold, int durationInDays) {
        // Return 0 early to prevent crashing on division by zero.
        if (durationInDays <= 0) {
            System.out.println("Error: Duration is " + durationInDays + ". Returning 0.0 to avoid ArithmeticException.");
            return 0.0;
        }

        // Calculation: Cast to double to get a precise decimal result instead of an integer truncation
        double velocity = (double) totalUnitsSold / durationInDays;

        // Logging: Visible traceability for debugging.
        System.out.println("Calculating Linear Velocity: " + totalUnitsSold + " units / " + durationInDays + " days = " + velocity);

        return velocity;
    }

    @Override
    public String getStrategyName() {
        return "Simple Linear Strategy";
    }
}