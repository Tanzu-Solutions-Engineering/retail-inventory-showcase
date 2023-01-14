package com.vmare.retail.inventory.consumer;


import com.vmare.retail.inventory.consumer.listener.TransactionConsumer;
import com.vmware.retail.inventory.domain.ProductReorder;
import com.vmware.retail.inventory.domain.StoreProductInventory;
import com.vmware.retail.inventory.domain.pos.Transaction;
import com.vmware.retail.inventory.repository.product.ProductReorderRepository;
import com.vmware.retail.inventory.repository.product.gemfire.ProductReorderGfRepository;
import com.vmware.retail.inventory.repository.store.StoreProductInventoryRepository;
import com.vmware.retail.inventory.repository.store.gemfire.StoreProductInvGfRepository;
import com.vmware.retail.inventory.repository.transaction.TransactionRepository;
import com.vmware.retail.inventory.repository.transaction.gemfire.TransactionGfRepository;
import com.vmware.retail.inventory.service.TransactionDataService;
import org.apache.geode.cache.CacheWriter;
import org.apache.geode.cache.DataPolicy;
import org.apache.geode.cache.GemFireCache;
import org.apache.geode.cache.Region;
import org.apache.geode.cache.client.ClientCacheFactory;
import org.apache.geode.pdx.ReflectionBasedAutoSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.gemfire.client.ClientRegionFactoryBean;
import org.springframework.data.gemfire.config.annotation.ClientCacheApplication;
import org.springframework.data.gemfire.config.annotation.EnablePdx;
import org.springframework.data.gemfire.repository.config.EnableGemfireRepositories;

import java.util.function.Supplier;


@Configuration
@ClientCacheApplication
@ComponentScan(basePackageClasses = TransactionDataService.class)
@EnablePdx
@EnableGemfireRepositories(basePackageClasses = {ProductReorderRepository.class,
        TransactionRepository.class, StoreProductInventoryRepository.class})
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
    ClientRegionFactoryBean<String, Transaction> transactionRegion(GemFireCache gemFireCache, CacheWriter writer)
    {
        var region = new ClientRegionFactoryBean();
        region.setCache(gemFireCache);
        region.setDataPolicy(DataPolicy.EMPTY);
        region.setName("Transaction");
        region.setCacheWriter(writer);
        return region;
    }

    @Bean
    ClientRegionFactoryBean<String, Transaction> storeProductInventoryRegion(GemFireCache gemFireCache)
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
    TransactionRepository transactionRepository()
    {
        Supplier<Region<String, Transaction>> supplier =
                () -> { return ClientCacheFactory.getAnyInstance().getRegion("Transaction");};

        return new TransactionGfRepository(supplier);
    }

    @Bean
    StoreProductInventoryRepository storeProductInventoryRepository()
    {
        Supplier<Region<String, StoreProductInventory>> supplier =
                () -> { return ClientCacheFactory.getAnyInstance().getRegion("StoreProductInventory");};

        return new StoreProductInvGfRepository(supplier);
    }

    @Bean
    public TransactionConsumer consumer(TransactionRepository transactionRepo,
                                        StoreProductInventoryRepository storeRepo)
    {
        return new TransactionConsumer(new TransactionDataService(transactionRepo, storeRepo));
    }
}
