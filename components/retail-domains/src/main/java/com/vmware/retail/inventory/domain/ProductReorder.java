package com.vmware.retail.inventory.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductReorder {
    private String id;
    private String productSku;
    private int currentAvailable;
    private int avgLeadTimeDays;
    private int maxLeadTimeDays;
    private int avgDailySales;
    private int maxDailySales;
    private int reorderLevel;
    private int orderQuantity;
    private long estDeliveryDays;
}
