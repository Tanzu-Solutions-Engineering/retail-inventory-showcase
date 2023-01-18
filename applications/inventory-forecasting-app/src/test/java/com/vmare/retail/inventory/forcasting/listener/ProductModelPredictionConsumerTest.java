package com.vmare.retail.inventory.forcasting.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vmware.retail.inventory.domain.StoreProductInventory;
import com.vmware.retail.inventory.ml.model.ProductReorderModelPrediction;
import com.vmware.retail.inventory.repository.product.ReorderInferenceRepository;
import com.vmware.retail.inventory.repository.store.StoreProductInventoryRepository;
import nyla.solutions.core.patterns.creational.generator.JavaBeanGeneratorCreator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductModelPredictionConsumerTest {

    @Mock
    private StoreProductInventoryRepository storeProductInventoryRepository;
    @Mock
    private ReorderInferenceRepository reorderInferenceRepository;
    private ProductReorderModelPrediction model = JavaBeanGeneratorCreator.of(ProductReorderModelPrediction.class).create();

    private StoreProductInventory storeProductInventory = JavaBeanGeneratorCreator.of(StoreProductInventory.class).create();
    private ProductModelPredictionConsumer subject;
    private ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        subject = new ProductModelPredictionConsumer(storeProductInventoryRepository,reorderInferenceRepository);
    }

    @DisplayName("Given mode, when accept then store model and update store remodel point")
    @Test
    void accept() throws JsonProcessingException {


        var json = mapper.writeValueAsString(model);
        System.out.println(json);

        when(storeProductInventoryRepository.findById(anyString())).thenReturn(Optional.of(storeProductInventory));
        subject.accept(model);

        verify(storeProductInventoryRepository).save(any(StoreProductInventory.class));
        verify(reorderInferenceRepository).save(any(ProductReorderModelPrediction.class));
    }
}