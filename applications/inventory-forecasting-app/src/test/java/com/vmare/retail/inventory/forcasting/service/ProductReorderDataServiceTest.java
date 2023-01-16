package com.vmare.retail.inventory.forcasting.service;

import com.vmare.retail.inventory.forcasting.repository.product.inference.ReorderInferenceRepository;
import com.vmare.retail.inventory.forcasting.repository.product.training.ReorderTrainingRepository;
import com.vmware.retail.inventory.domain.ProductReorder;
import com.vmware.retail.inventory.domain.StoreProductInventory;
import com.vmware.retail.inventory.ml.model.ProductReorderModelPrediction;
import com.vmware.retail.inventory.repository.product.ProductReorderRepository;
import nyla.solutions.core.patterns.creational.generator.JavaBeanGeneratorCreator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductReorderDataServiceTest {

    @Mock
    private ProductReorderRepository productReorderRepository;

    @Mock
    private ReorderInferenceRepository reorderInferenceRepository;

    @Mock
    private ReorderTrainingRepository reorderTrainingRepository;

    private ProductReorderDataService subject;
    private StoreProductInventory inventory = JavaBeanGeneratorCreator.of(StoreProductInventory.class).create();
    private ProductReorderModelPrediction model = JavaBeanGeneratorCreator.of(ProductReorderModelPrediction.class).create();


    @BeforeEach
    void setUp() {
        subject = new ProductReorderDataService(productReorderRepository,
                reorderInferenceRepository,
                reorderTrainingRepository);
    }

    @DisplayName("Given model doesn't exist when submitReorder then train model and create initial order")
    @Test
    void submitReorder() {


       when(reorderTrainingRepository.train(any())).thenReturn(model);


        subject.submitReorder(inventory);

        verify(reorderTrainingRepository).train(inventory);

        verify(productReorderRepository).save(any(ProductReorder.class));

    }
}