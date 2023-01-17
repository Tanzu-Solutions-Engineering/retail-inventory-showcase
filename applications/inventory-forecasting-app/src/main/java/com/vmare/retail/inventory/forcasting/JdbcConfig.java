package com.vmare.retail.inventory.forcasting;

import com.vmware.retail.inventory.repository.database.store.product.StoreProductInventoryJdbcRepository;
import com.vmware.retail.inventory.repository.database.transaction.TransactionJdbcRepository;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses =
        {StoreProductInventoryJdbcRepository.class})
public class JdbcConfig {
}
