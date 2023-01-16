package com.vmare.retail.inventory.forcasting;

import com.vmare.retail.inventory.forcasting.repository.product.inference.ReorderInferenceRepository;
import com.vmare.retail.inventory.forcasting.repository.product.inference.gemfire.ReorderInferenceGemFireRepository;
import com.vmware.retail.inventory.domain.ProductReorder;
import com.vmware.retail.inventory.ml.model.ProductReorderModelPrediction;
import com.vmware.retail.inventory.repository.product.ProductReorderRepository;
import com.vmware.retail.inventory.repository.product.gemfire.ProductReorderGfRepository;
import org.apache.geode.cache.DataPolicy;
import org.apache.geode.cache.GemFireCache;
import org.apache.geode.cache.Region;
import org.apache.geode.cache.client.ClientCacheFactory;
import org.apache.geode.pdx.ReflectionBasedAutoSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.gemfire.client.ClientRegionFactoryBean;
import org.springframework.data.gemfire.config.annotation.ClientCacheApplication;
import org.springframework.data.gemfire.config.annotation.EnablePdx;

import java.util.function.Supplier;

@Configuration
@EnablePdx
@ClientCacheApplication
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
    ReorderInferenceRepository reorderInferenceRepository()
    {
        Supplier<Region<String, ProductReorderModelPrediction>> supplier
                = () -> { return ClientCacheFactory.getAnyInstance().getRegion("ProductReorderModelPrediction");};
        return new ReorderInferenceGemFireRepository(supplier);
    }

    @Bean
    ProductReorderRepository productReorderRepository()
    {
        Supplier<Region<String, ProductReorder>> supplier =
                () -> { return ClientCacheFactory.getAnyInstance().getRegion("ProductReorder");};

        return new ProductReorderGfRepository(supplier);
    }

}
