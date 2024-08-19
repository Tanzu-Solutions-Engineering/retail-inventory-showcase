package io.spring.retail.product;

import com.vmware.retail.product.domain.Product;
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
    public String locators;
    @Bean
    ClientRegionFactoryBean<String, Product> productRegion(GemFireCache cache)
    {
        var region = new ClientRegionFactoryBean<String,Product>();
        region.setName("Product");
        region.setCache(cache);
        region.setDataPolicy(DataPolicy.EMPTY);
        return region;

    }
}
