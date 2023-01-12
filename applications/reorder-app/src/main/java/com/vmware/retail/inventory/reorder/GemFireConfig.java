package com.vmware.retail.inventory.reorder;


import com.vmware.retail.inventory.domain.ProductReorder;
import com.vmware.retail.inventory.repository.product.ProductReorderRepository;
import org.apache.geode.cache.DataPolicy;
import org.apache.geode.cache.GemFireCache;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.gemfire.client.ClientRegionFactoryBean;
import org.springframework.data.gemfire.config.annotation.ClientCacheApplication;
import org.springframework.data.gemfire.repository.config.EnableGemfireRepositories;

@Configuration
@ClientCacheApplication
@EnableGemfireRepositories(basePackageClasses = ProductReorderRepository.class)
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
