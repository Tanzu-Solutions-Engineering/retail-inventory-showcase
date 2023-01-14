package com.vmware.retail.inventory.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class StoreProductInventory {
    private String id;
    private String productId;
    private String storeId;
    private long currentAvailable;
    private ProductReorder reorderPoint;

}
