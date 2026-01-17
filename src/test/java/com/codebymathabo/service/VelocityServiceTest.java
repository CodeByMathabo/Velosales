package com.codebymathabo.service;

import com.codebymathabo.model.Sale;
import com.codebymathabo.repository.SalesRepository;
import com.codebymathabo.strategy.VelocityStrategy;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;

// @ExtendWith(MockitoExtension.class): Tells JUnit to enable the @Mock annotations.
@ExtendWith(MockitoExtension.class)
class VelocityServiceTest {

    // Mock the database so we don't need a real connection for testing.
    @Mock
    private SalesRepository salesRepository;

    // Mock the strategy so we test the Service flow, not the math formula itself.
    @Mock
    private VelocityStrategy velocityStrategy;

    // Inject the mocks above into the real VelocityService instance.
    @InjectMocks
    private VelocityService velocityService;

    @Test
    void calculateVelocityForBatch_ShouldReturnCorrectResults() {
        // Arrange: Setup fake data.
        String productId = "ITEM-TEST";
        List<String> inputList = Collections.singletonList(productId);

        // Create a fake sale record to return from the "database".
        Sale fakeSale = new Sale(productId, 10, LocalDate.now());
        List<Sale> fakeDbResponse = Collections.singletonList(fakeSale);

        // Tell the mock repository: "When someone asks for sales, return this fake list."
        Mockito.when(salesRepository.findByProductIdInAndSaleDateAfter(Mockito.anyList(), Mockito.any(LocalDate.class)))
                .thenReturn(fakeDbResponse);

        // Tell the mock strategy: "When asked to calculate, just return 5.0."
        Mockito.when(velocityStrategy.calculate(Mockito.anyLong(), Mockito.anyInt()))
                .thenReturn(5.0);

        // Tells the mock strategy to return a name for the logging call.
        Mockito.when(velocityStrategy.getStrategyName()).thenReturn("Mock Strategy");

        // Act: Run the actual service method.
        Map<String, Double> result = velocityService.calculateVelocityForBatch(inputList);

        // Assert: Verify the result is what we expected.
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(5.0, result.get(productId));

        // Verify: Ensure the service actually called the database (graduate level check).
        Mockito.verify(salesRepository).findByProductIdInAndSaleDateAfter(Mockito.anyList(), Mockito.any(LocalDate.class));
    }

    @Test
    void calculateVelocityForBatch_ShouldHandleEmptyInput() {
        // Act: Call with empty list.
        Map<String, Double> result = velocityService.calculateVelocityForBatch(Collections.emptyList());

        // Assert: Should return empty map immediately without errors.
        Assertions.assertTrue(result.isEmpty());

        // Verify: Ensure we NEVER touched the database (efficiency check).
        Mockito.verifyNoInteractions(salesRepository);
    }
}