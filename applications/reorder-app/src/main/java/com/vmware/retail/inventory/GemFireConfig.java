package com.vmware.retail.inventory;


import com.vmware.retail.inventory.domain.ProductReorder;
import org.apache.geode.cache.DataPolicy;
import org.apache.geode.cache.GemFireCache;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.gemfire.client.ClientRegionFactoryBean;
import org.springframework.data.gemfire.config.annotation.ClientCacheApplication;

@Configuration
@ClientCacheApplication
public class GemFireConfig {

    @Value("${spring.data.gemfire.pool.default.locators}")
    private String locators;

    @Bean
    ClientRegionFactoryBean<String, ProductReorder> productReorder(GemFireCache gemFireCache)
    {
        var region = new ClientRegionFactoryBean();
        region.setCache(gemFireCache);
        region.setDataPolicy(DataPolicy.EMPTY);
        region.setName("ProductReorder");
        return region;
    }
}
