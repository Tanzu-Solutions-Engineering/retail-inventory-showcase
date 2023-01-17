package com.vmware.retail.inventory.domain.pos;

import java.time.LocalDateTime;

public record POSTransaction(String storeId,
                             String registerId,
                             Double total,
                             String itemId,
                             LocalDateTime timestamp) {
}
