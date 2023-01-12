package com.vmare.retail.inventory.forcasting.listener;

import com.vmware.retail.inventory.domain.StoreProductInventory;
import com.vmware.retail.inventory.repository.product.ProductReorderRepository;
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
    private final ProductReorderRepository productReorderRepository;

    @ContinuousQuery(query =
            "select * " +
            "from /StoreProductInventory " +
            "where currentAvailable <= reorderPoint.reorderLevel")
    public void check(CqEvent cqEvent) {

        if(cqEvent.getBaseOperation().isDestroy())
            return;

        StoreProductInventory inventory = (StoreProductInventory)cqEvent.getNewValue();

        productReorderRepository.save(inventory.getReorderPoint());
    }
}
