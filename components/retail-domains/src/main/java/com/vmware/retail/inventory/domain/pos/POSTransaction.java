package com.vmware.retail.inventory.domain.pos;

public record POSTransaction(String storeId,
                             String registerId,
                             Double total,
                             String itemId) {
}
