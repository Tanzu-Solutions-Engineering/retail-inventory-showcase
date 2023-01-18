package com.vmare.retail.inventory.forcasting;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vmware.retail.inventory.domain.ProductReorder;
import com.vmware.retail.inventory.domain.StoreProductInventory;
import com.vmware.retail.inventory.ml.model.ProductReorderModelPrediction;
import com.vmware.retail.inventory.repository.database.store.product.StoreProductInventoryJdbcRepository;
import com.vmware.retail.inventory.repository.product.ProductReorderRepository;
import com.vmware.retail.inventory.repository.product.ReorderInferenceRepository;
import com.vmware.retail.inventory.repository.product.gemfire.ProductReorderGfRepository;
import com.vmware.retail.inventory.repository.product.gemfire.ReorderInferenceGemFireRepository;
import com.vmware.retail.inventory.repository.store.StoreProductInventoryRepository;
import com.vmware.retail.inventory.repository.store.gemfire.StoreProductInvGfRepository;
import com.vmware.retail.inventory.repository.writer.StoreProductInventoryCacheWriter;
import org.apache.geode.cache.DataPolicy;
import org.apache.geode.cache.GemFireCache;
import org.apache.geode.cache.Region;
import org.apache.geode.cache.client.ClientCacheFactory;
import org.apache.geode.pdx.ReflectionBasedAutoSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.gemfire.client.ClientRegionFactoryBean;
import org.springframework.data.gemfire.config.annotation.ClientCacheApplication;
import org.springframework.data.gemfire.config.annotation.EnableContinuousQueries;
import org.springframework.data.gemfire.config.annotation.EnablePdx;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.function.Supplier;

@Configuration
@EnablePdx
@ClientCacheApplication(subscriptionEnabled = true)
@EnableContinuousQueries
public class GemFireConfig {

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
    StoreProductInventoryCacheWriter storeProductInventoryCacheWriter(NamedParameterJdbcTemplate template, ObjectMapper objectMapper)
    {
        return new StoreProductInventoryCacheWriter(new StoreProductInventoryJdbcRepository(template,objectMapper));
    }

    //StoreProductInventory
    @Bean
    ClientRegionFactoryBean<String, StoreProductInventory> storeProductInventory(GemFireCache gemFireCache, StoreProductInventoryCacheWriter writer)
    {
        var region = new ClientRegionFactoryBean();
        region.setCache(gemFireCache);
        region.setDataPolicy(DataPolicy.EMPTY);
        region.setName("StoreProductInventory");
        region.setCacheWriter(writer);
        return region;
    }
    //ProductReorderModelPrediction
    @Bean
    ClientRegionFactoryBean<String, ProductReorderModelPrediction> productReorderModelPredictionRegion(GemFireCache gemFireCache)
    {
        var region = new ClientRegionFactoryBean();
        region.setCache(gemFireCache);
        region.setDataPolicy(DataPolicy.EMPTY);
        region.setName("ProductReorderModelPrediction");
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

    @Bean
    ReorderInferenceRepository reorderInferenceRepository()
    {
        Supplier<Region<String, ProductReorderModelPrediction>> supplier
                = () -> { return ClientCacheFactory.getAnyInstance().getRegion("ProductReorderModelPrediction");};
        return new ReorderInferenceGemFireRepository(supplier);
    }


}
