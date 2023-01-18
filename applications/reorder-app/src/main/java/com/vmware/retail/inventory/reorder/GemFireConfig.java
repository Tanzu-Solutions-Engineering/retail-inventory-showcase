package com.vmware.retail.inventory.reorder;


import com.vmware.retail.inventory.domain.ProductReorder;
import com.vmware.retail.inventory.domain.StoreProductInventory;
import com.vmware.retail.inventory.repository.product.ProductReorderRepository;
import com.vmware.retail.inventory.repository.product.gemfire.ProductReorderGfRepository;
import com.vmware.retail.inventory.repository.store.StoreProductInventoryRepository;
import com.vmware.retail.inventory.repository.store.gemfire.StoreProductInvGfRepository;
import com.vmware.retail.inventory.repository.writer.StoreProductInventoryCacheWriter;
import org.apache.geode.cache.DataPolicy;
import org.apache.geode.cache.GemFireCache;
import org.apache.geode.cache.Region;
import org.apache.geode.cache.client.ClientCacheFactory;
import org.apache.geode.pdx.ReflectionBasedAutoSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.gemfire.client.ClientRegionFactoryBean;
import org.springframework.data.gemfire.config.annotation.ClientCacheApplication;
import org.springframework.data.gemfire.config.annotation.EnablePdx;
import org.springframework.data.gemfire.repository.config.EnableGemfireRepositories;

import java.util.function.Supplier;

@Configuration
@EnablePdx
@ClientCacheApplication
@EnableGemfireRepositories(basePackageClasses = ProductReorderRepository.class)
public class GemFireConfig {

    @Value("${spring.data.gemfire.pool.default.locators}")
    private String locators;

    @Bean
    ReflectionBasedAutoSerializer pdxSerializer()
    {
        return new ReflectionBasedAutoSerializer(".*");
    }

    @Bean
    ClientRegionFactoryBean<String, ProductReorder> productReorder(GemFireCache gemFireCache)
    {
        var region = new ClientRegionFactoryBean();
        region.setCache(gemFireCache);
        region.setDataPolicy(DataPolicy.EMPTY);
        region.setName("ProductReorder");
        return region;
    }

    @Bean
    ClientRegionFactoryBean<String, StoreProductInventory> storeProductInventory(GemFireCache gemFireCache)
    {
        var region = new ClientRegionFactoryBean();
        region.setCache(gemFireCache);
        region.setDataPolicy(DataPolicy.EMPTY);
        region.setName("StoreProductInventory");
        return region;
    }

    @Bean
    ProductReorderRepository productReorderRepository()
    {
        Supplier<Region<String, ProductReorder>> supplier =
                () -> { return ClientCacheFactory.getAnyInstance().getRegion("ProductReorder");};

        return new ProductReorderGfRepository(supplier);
    }

    @Bean
    StoreProductInventoryRepository storeProductInventoryRepository()
    {
        Supplier<Region<String, StoreProductInventory>> supplier =
                () -> { return ClientCacheFactory.getAnyInstance().getRegion("StoreProductInventory");};
        return new StoreProductInvGfRepository(supplier);
    }
}
