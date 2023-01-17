package com.vmware.retail.inventory.repository.product.gemfire;

import com.vmware.retail.inventory.domain.ProductReorder;
import com.vmware.retail.inventory.repository.product.ProductReorderRepository;
import nyla.solutions.core.patterns.creational.generator.JavaBeanGeneratorCreator;
import org.apache.geode.cache.Region;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;
import java.util.function.Supplier;

import static java.util.Arrays.asList;
import static nyla.solutions.core.util.Organizer.toMap;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductReorderGfRepositoryTest {

    private ProductReorderGfRepository subject;
    private Supplier<Region<String, ProductReorder>> regionSupplier;

    @Mock
    private Region<String,ProductReorder> region;
    private ProductReorder productReorder = JavaBeanGeneratorCreator
            .of(ProductReorder.class)
            .create();

    @BeforeEach
    void setUp() {
        regionSupplier = () -> {return region;};

        subject = new ProductReorderGfRepository(regionSupplier);
    }

    @Test
    void findAll() {
        Iterable<ProductReorder> expected = asList(productReorder);

        Map<String, ProductReorder> map = toMap("1",productReorder);

        when(region.getAll(any())).thenReturn(map);



        var actual = subject.findAll();

        assertEquals(actual.iterator().next(),productReorder);
    }

    @Test
    void save() {
        subject.save(productReorder);

        verify(region).put(anyString(),any());
    }
}