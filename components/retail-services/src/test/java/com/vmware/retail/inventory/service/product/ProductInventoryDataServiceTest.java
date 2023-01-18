package com.vmware.retail.inventory.service.product;

import com.vmware.retail.inventory.domain.ProductReorder;
import com.vmware.retail.inventory.domain.StoreProductInventory;
import com.vmware.retail.inventory.ml.model.ProductModelInventory;
import com.vmware.retail.inventory.repository.product.ProductReorderRepository;
import com.vmware.retail.inventory.repository.product.ReorderInferenceRepository;
import com.vmware.retail.inventory.repository.store.StoreProductInventoryRepository;
import nyla.solutions.core.patterns.creational.generator.JavaBeanGeneratorCreator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

/**
 * Testing for ProductInventoryDataService
 * @author gregory green
 */
@ExtendWith(MockitoExtension.class)
class ProductInventoryDataServiceTest {

    private ProductInventoryDataService subject;

    @Mock
    private StoreProductInventoryRepository storeProductInventoryRepository;

    @Mock
    private ReorderInferenceRepository reorderInferenceRepository;

    @Mock
    private ProductReorderRepository productReorderRepository;
    private ProductModelInventory productModelInventory = JavaBeanGeneratorCreator.of(ProductModelInventory.class).create();
    private StoreProductInventory storeProductInventory = JavaBeanGeneratorCreator.of(StoreProductInventory.class).create();
    private ProductReorder productReorder = JavaBeanGeneratorCreator.of(ProductReorder.class).create();

    @BeforeEach
    void setUp() {
        subject = new ProductInventoryDataService(storeProductInventoryRepository,reorderInferenceRepository,productReorderRepository);
    }

    @Test
    void findAllProductModelInventories() {

        Iterable<StoreProductInventory> expectStoreProductInventories = asList(productModelInventory.storeProductInventory());


        when(storeProductInventoryRepository.findAll()).thenReturn(expectStoreProductInventories);

        when(reorderInferenceRepository
                .findByProductIdAndStoreId(anyString(),anyString()))
                .thenReturn(Optional.of(productModelInventory.model()));

        var actual = subject.findAllProductModelInventories();

        List<ProductModelInventory> expected = asList(productModelInventory);

        assertEquals(expected.get(0).storeProductInventory(), actual.iterator().next().storeProductInventory());
    }

    @Test
    void findAllProductReorders() {

        List<ProductReorder> expected = asList(productReorder);

        when(productReorderRepository.findAll()).thenReturn(expected);

        var actual = subject.findAllProductReorders();

        assertEquals(expected, actual);
    }
}