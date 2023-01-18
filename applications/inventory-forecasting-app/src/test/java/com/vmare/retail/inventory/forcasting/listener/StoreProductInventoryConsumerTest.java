package com.vmare.retail.inventory.forcasting.listener;

import com.vmare.retail.inventory.forcasting.repository.product.training.ReorderTrainingRepository;
import com.vmware.retail.inventory.domain.StoreProductInventory;
import com.vmware.retail.inventory.ml.model.ProductReorderModelPrediction;
import com.vmware.retail.inventory.repository.product.ProductReorderRepository;
import com.vmware.retail.inventory.repository.store.StoreProductInventoryRepository;
import nyla.solutions.core.patterns.creational.generator.JavaBeanGeneratorCreator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StoreProductInventoryConsumerTest {

    private StoreProductInventory storeProductInventory = JavaBeanGeneratorCreator.of(StoreProductInventory.class)
            .create();

    @Mock
    private StoreProductInventoryRepository storeProductInventoryRepository;

    @Mock
    private ReorderTrainingRepository reorderTrainingRepository;

    private StoreProductInventoryConsumer subject;
    private ProductReorderModelPrediction model = JavaBeanGeneratorCreator.of(ProductReorderModelPrediction.class).create();

    @Mock
    private ProductReorderRepository productReorderRepository;

    @BeforeEach
    void setUp() {
        subject = new StoreProductInventoryConsumer(storeProductInventoryRepository,
                reorderTrainingRepository,productReorderRepository);

    }

    @DisplayName("Given storeProductInventory without reorderPoint when accept then calculateReorder and save")
    @Test
    void accept() {

        when(this.reorderTrainingRepository.train(any())).thenReturn(model);

        subject.accept(storeProductInventory);

        verify(reorderTrainingRepository).train(any());

        verify(productReorderRepository).deleteById(anyString());

        verify(storeProductInventoryRepository).save(any(StoreProductInventory.class));
    }
}