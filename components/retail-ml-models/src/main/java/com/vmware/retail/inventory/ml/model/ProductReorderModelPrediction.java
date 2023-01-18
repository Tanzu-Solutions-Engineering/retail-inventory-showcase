package com.vmware.retail.inventory.ml.model;

import com.vmware.retail.inventory.domain.ProductReorder;
import com.vmware.retail.inventory.domain.StoreProductInventory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductReorderModelPrediction {

    private String id;

    private String storeId;
    private String productId;
    private int averageDailySales;
    private int averageDailyOrders;
    private int averageLeadTimeDays;
    private int maxDailyOrders;
    private int maxLeadTimeDays;
    private int maxDailySales;


    public ProductReorder inference(StoreProductInventory storeProductInventory)
    {
        return ProductReorder.builder()
                .productSku(storeProductInventory.getProductId())
                .reorderLevel(storeProductInventory.getReorderPoint()) //calculate
                .avgDailySales(averageDailySales)
                .avgLeadTimeDays(averageLeadTimeDays)
                .orderQuantity(calculateReorderQuantity()) //calculate
                .id(storeProductInventory.getId())
                .storeId(storeProductInventory.getStoreId())
                .maxDailySales(maxDailySales)
                .currentAvailable(storeProductInventory.getCurrentAvailable())
                .maxLeadTimeDays(maxLeadTimeDays).build();
    }

    /**
     * Optimal Reorder Quantity for a SKU = Avg. Daily Units Sold  x Avg. Lead Time
     * @return Optimal Reorder Quantity for a SKU = Avg. Daily Units Sold  x Avg. Lead Time
     */
    public int calculateReorderQuantity() {
        return averageDailySales * averageLeadTimeDays;
    }

    public int calculateReorderPoint(){
        return demandDuringLeadTime() + safetyStock();
    }



    protected int safetyStock() {
        return (maxDailyOrders*maxLeadTimeDays) - (averageDailyOrders*averageLeadTimeDays);
    }

    protected  int demandDuringLeadTime() {
        return averageLeadTimeDays*averageDailySales;
    }


}
