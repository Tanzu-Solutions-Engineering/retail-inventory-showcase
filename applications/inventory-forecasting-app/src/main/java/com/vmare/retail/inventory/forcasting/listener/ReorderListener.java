package com.vmare.retail.inventory.forcasting.listener;

import com.vmare.retail.inventory.forcasting.service.ProductReorderService;
import com.vmware.retail.inventory.domain.StoreProductInventory;
import lombok.RequiredArgsConstructor;
import org.apache.geode.cache.query.CqEvent;
import org.springframework.data.gemfire.listener.annotation.ContinuousQuery;
import org.springframework.stereotype.Component;

/**
 * @author gregory green
 */
@RequiredArgsConstructor
@Component
public class ReorderListener {
    private final ProductReorderService productReorderService;

    @ContinuousQuery(query =
            "select * " +
            "from /StoreProductInventory " +
            "where currentAvailable <= reorderPoint.reorderLevel")
    public void check(CqEvent cqEvent) {

        if(cqEvent.getBaseOperation().isDestroy())
            return;

        StoreProductInventory inventory = (StoreProductInventory)cqEvent.getNewValue();

        productReorderService.submitReorder(inventory);
    }
}
