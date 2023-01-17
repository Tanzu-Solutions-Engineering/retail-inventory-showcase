package com.vmare.retail.inventory.consumer;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.vmare.retail.inventory.consumer.listener.TransactionConsumer;
import com.vmare.retail.inventory.consumer.writer.TransactionDbCacheWriter;
import com.vmware.retail.inventory.domain.ProductReorder;
import com.vmware.retail.inventory.domain.StoreProductInventory;
import com.vmware.retail.inventory.domain.pos.Transaction;
import com.vmware.retail.inventory.repository.database.store.product.StoreProductInventoryJdbcRepository;
import com.vmware.retail.inventory.repository.product.ProductReorderRepository;
import com.vmware.retail.inventory.repository.product.gemfire.ProductReorderGfRepository;
import com.vmware.retail.inventory.repository.store.StoreProductInventoryRepository;
import com.vmware.retail.inventory.repository.store.gemfire.StoreProductInvGfRepository;
import com.vmware.retail.inventory.repository.transaction.TransactionRepository;
import com.vmware.retail.inventory.repository.transaction.gemfire.TransactionGfRepository;
import com.vmware.retail.inventory.repository.writer.StoreProductInventoryCacheWriter;
import com.vmware.retail.inventory.service.transaction.TransactionDataService;
import nyla.solutions.core.patterns.integration.Publisher;
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
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.function.Supplier;


@Configuration
@ClientCacheApplication
@ComponentScan(basePackageClasses = TransactionDataService.class)
@EnablePdx
@EnableGemfireRepositories(basePackageClasses = {ProductReorderRepository.class,
        TransactionRepository.class})
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
    ClientRegionFactoryBean<String, Transaction> transactionRegion(GemFireCache gemFireCache, TransactionDbCacheWriter writer)
    {
        var region = new ClientRegionFactoryBean();
        region.setCache(gemFireCache);
        region.setDataPolicy(DataPolicy.EMPTY);
        region.setName("Transaction");
        region.setCacheWriter(writer);
        return region;
    }

    @Bean
    StoreProductInventoryCacheWriter storeProductInventoryCacheWriter(NamedParameterJdbcTemplate template, ObjectMapper objectMapper)
    {
        return new StoreProductInventoryCacheWriter(new StoreProductInventoryJdbcRepository(template,objectMapper));
    }

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
                                        StoreProductInvGfRepository storeRepo,
                                        Publisher<StoreProductInventory> publisher)
    {
        return new TransactionConsumer(new TransactionDataService(transactionRepo, storeRepo, publisher));
    }
}
