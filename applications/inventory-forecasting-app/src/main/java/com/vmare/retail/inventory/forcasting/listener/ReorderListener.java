package com.vmare.retail.inventory.forcasting.listener;

import com.vmare.retail.inventory.forcasting.service.ProductReorderService;
import com.vmware.retail.inventory.domain.StoreProductInventory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.geode.cache.query.CqEvent;
import org.springframework.data.gemfire.listener.annotation.ContinuousQuery;
import org.springframework.stereotype.Component;

/**
 * @author gregory green
 */
@Component
@Slf4j
public class ReorderListener {
    private final ProductReorderService productReorderService;

    public ReorderListener(ProductReorderService productReorderService){
        this.productReorderService = productReorderService;
    }

    @ContinuousQuery(query =
            "select *                                           " +
            "from /StoreProductInventory                        "+
            "where currentAvailable <= reorderPoint")
    public void check(CqEvent cqEvent) {

        log.info("EVENT: {}",cqEvent);

        if(cqEvent.getBaseOperation().isDestroy() ||
                cqEvent.getQueryOperation().isDestroy())
            return;

        StoreProductInventory inventory = (StoreProductInventory)cqEvent.getNewValue();

        log.info("Submitting reorder for {}",inventory);

        productReorderService.submitReorder(inventory);
    }
}
