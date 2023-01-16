package com.vmware.retail.inventory.ml.model;

import com.vmware.retail.inventory.domain.ProductReorder;
import com.vmware.retail.inventory.domain.StoreProductInventory;
import nyla.solutions.core.patterns.creational.generator.JavaBeanGeneratorCreator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 *
 * Testing for ProductReorderModelPrediction
 * @author gregory green
 */
class ProductReorderModelPredictionTest {
    private ProductReorderModelPrediction subject;
    private StoreProductInventory storeProductInventory = JavaBeanGeneratorCreator.of(StoreProductInventory.class).create();
    private int averageDailySales = 20;
    private int maxDailyOrders = 24;
    private int maxLeadTimeDays = 20;
    private int averageDailyOrders = 2;
    private int averageLeadTimeDays = 5;
    private int maxDailySales = 23;
    private long estDeliveryDays = 3;

    @BeforeEach
    void setUp() {
        subject = new ProductReorderModelPrediction();
    }

    @Test
    void inference() {

        subject.setAverageDailyOrders(averageDailyOrders);
        subject.setAverageDailySales(averageDailySales);
        subject.setAverageLeadTimeDays(averageLeadTimeDays);
        subject.setMaxLeadTimeDays(maxLeadTimeDays);
        subject.setMaxDailySales(maxDailySales);

        int expectOrderQuantity = 100;
        int expectedReorderLevel = 90;
        var  expected = ProductReorder.builder()
                .id(storeProductInventory.getId())
                .avgDailySales(averageDailySales)
                .storeId(storeProductInventory.getStoreId())
                .avgLeadTimeDays(averageLeadTimeDays)
                .productSku(storeProductInventory.getProductId())
                .currentAvailable(storeProductInventory.getCurrentAvailable())
                .maxDailySales(maxDailySales)
                .maxLeadTimeDays(maxLeadTimeDays)
                .orderQuantity(expectOrderQuantity)
                .reorderLevel(expectedReorderLevel)
                .build();


        var actual = subject.inference(storeProductInventory);

        assertEquals(expected, actual);

    }

    @DisplayName("Reorder Point (ROP) = Demand during lead time + safety stock")
    @Test
    void reorderPoint() {
        var expected = 570;

        subject.setAverageDailySales(averageDailySales);

        subject.setMaxDailyOrders(maxDailyOrders);
        subject.setMaxLeadTimeDays(maxLeadTimeDays);

        subject.setAverageDailyOrders(averageDailyOrders);
        subject.setAverageLeadTimeDays(averageLeadTimeDays);

        var actual = subject.calculateReorderPoint();

        assertEquals(expected, actual);
    }

    @Test
    void calculateReorderQuantity() {
        //Optimal Reorder Quantity for a SKU = Avg. Daily Units Sold  x Avg. Lead Time

        subject.setAverageDailySales(averageDailySales);
        subject.setAverageLeadTimeDays(averageLeadTimeDays);

        var expected = averageDailySales * averageLeadTimeDays;
        var actual = subject.calculateReorderQuantity();

        assertEquals(expected, actual);
    }

    @DisplayName("Lead time demand = lead time x average daily sales")
    @Test
    void leadTimeDemand() {
        var expected = averageLeadTimeDays * averageDailySales;

        subject.setAverageLeadTimeDays(averageLeadTimeDays);
        subject.setAverageDailySales(averageDailySales);

        var actual = subject.demandDuringLeadTime();

        assertEquals(expected, actual);
    }


    @DisplayName("Safety stock level = (Max daily orders x max lead time) – (average daily orders x average lead time).")
    @Test
    void safetyStockLevel() {

        subject.setMaxDailyOrders(maxDailyOrders);
        subject.setMaxLeadTimeDays(maxLeadTimeDays);

        subject.setAverageDailyOrders(averageDailyOrders);
        subject.setAverageLeadTimeDays(averageLeadTimeDays);

        var expected = (maxDailyOrders*maxLeadTimeDays) - (averageDailyOrders * averageLeadTimeDays);
        var actual = subject.safetyStock();

        assertEquals(expected, actual);

    }
}