package com.vmware.retail.inventory.ml.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReorderModelPrediction {

    private int leadTime;
    private int averageDailySales;
    private int maxDailyOrders;
    private int maxLeadTime;
    private int averageDailyOrders;
    private int averageLeadTime;

    public int reorderPoint(){
        return demandDuringLeadTime() + safetyStock();
    }

    private int safetyStock() {
        return (maxDailyOrders*maxLeadTime) - (averageDailyOrders*averageLeadTime);
    }

    private int demandDuringLeadTime() {
        return leadTime*averageDailySales;
    }
}
