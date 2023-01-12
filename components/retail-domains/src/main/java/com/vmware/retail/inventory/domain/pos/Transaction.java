package com.vmware.retail.inventory.domain.pos;

public record Transaction(String storeId, String registerId, Double total,String itemId) {
}
