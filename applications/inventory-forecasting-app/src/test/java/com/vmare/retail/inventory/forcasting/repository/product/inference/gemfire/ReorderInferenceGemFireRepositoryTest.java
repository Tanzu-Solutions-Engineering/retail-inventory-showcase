package com.vmare.retail.inventory.forcasting.repository.product.inference.gemfire;

import com.vmware.retail.inventory.ml.model.ProductReorderModelPrediction;
import nyla.solutions.core.patterns.creational.generator.JavaBeanGeneratorCreator;
import org.apache.geode.cache.Region;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReorderInferenceGemFireRepositoryTest {

    private ReorderInferenceGemFireRepository subject;

    @Mock
    private Region<String, ProductReorderModelPrediction> region;
    private Supplier<Region<String, ProductReorderModelPrediction>> supplier = () -> { return region;};
    private ProductReorderModelPrediction model = JavaBeanGeneratorCreator.of(ProductReorderModelPrediction.class).create();
    private String productId = "p";
    private String storeId = "s";

    @BeforeEach
    void setUp() {
        subject = new ReorderInferenceGemFireRepository(supplier);
    }

    @Test
    void findByProductIdAndStoreId() {

        when(region.get(anyString())).thenReturn(model);

        var actual = subject.findByProductIdAndStoreId(productId,storeId);
        assertEquals(model, actual.get());
    }

    @Test
    void findByProductIdAndStoreId_when_null_then_returnEmpty() {

        var actual = subject.findByProductIdAndStoreId(productId,storeId);
        assertTrue(actual.isEmpty());
    }


    @Test
    void save() {

        subject.save(model);

        verify(region).put(anyString(),any(ProductReorderModelPrediction.class));
    }
}