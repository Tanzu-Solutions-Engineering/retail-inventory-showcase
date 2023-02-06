package io.spring.retail.product;

import io.spring.retail.product.domain.Product;
import org.apache.geode.cache.DataPolicy;
import org.apache.geode.cache.GemFireCache;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.gemfire.client.ClientRegionFactoryBean;
import org.springframework.data.gemfire.config.annotation.ClientCacheApplication;

@Configuration
@ClientCacheApplication
public class GemFireConfig {

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
