package com.vmare.retail.inventory.consumer;

import com.vmware.retail.inventory.repository.database.transaction.TransactionJdbcRepository;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses =
        {TransactionJdbcRepository.class})
public class JdbcConfig {
}
