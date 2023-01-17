package com.vmare.retail.inventory.forcasting.repository.product.training.jdbc;

import com.vmware.retail.inventory.domain.StoreProductInventory;
import com.vmware.retail.inventory.ml.model.ProductReorderModelPrediction;
import nyla.solutions.core.patterns.creational.generator.JavaBeanGeneratorCreator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class ReorderTrainingJdbcRepositoryTest {

    private ReorderTrainingJdbcRepository subject;

    @Mock
    private NamedParameterJdbcTemplate template;
    private ProductReorderModelPrediction model = JavaBeanGeneratorCreator
            .of(ProductReorderModelPrediction.class).create();
    private int currentAvailable = 23;
    private StoreProductInventory storeProductInventory = StoreProductInventory.builder()
            .storeId(model.getStoreId())
            .productId(model.getProductId())
            .currentAvailable(currentAvailable)
            .build();

    @DisplayName("Given inventory when train then calculate trends and return model")
    @Test
    void train() {

        subject = new ReorderTrainingJdbcRepository(template);

        model.setId(storeProductInventory.getId());

        var actual = subject.train(storeProductInventory);

        assertNotNull(actual);
    }
}