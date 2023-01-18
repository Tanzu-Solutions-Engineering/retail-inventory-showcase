package com.vmware.retail.inventory.controller;

import com.vmware.retail.inventory.domain.ProductReorder;
import com.vmware.retail.inventory.reorder.controller.InventoryController;
import com.vmware.retail.inventory.repository.product.ProductReorderRepository;
import com.vmware.retail.inventory.repository.store.StoreProductInventoryRepository;
import com.vmware.retail.inventory.service.product.ProductInventoryService;
import nyla.solutions.core.patterns.creational.generator.JavaBeanGeneratorCreator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InventoryControllerTest {


    @Mock
    private InventoryController subject;

    private ThreadFactory factory = Executors.defaultThreadFactory();

    private ProductReorder expectedProductReorder = JavaBeanGeneratorCreator.of(ProductReorder.class).create();
    private long frequency = 3;

    @Mock
    private ProductInventoryService service;

    @Test
    void when_streamOrders_then_return_list() {

        when(service.findAllProductReorders()).thenReturn(asList(expectedProductReorder));

        subject = new InventoryController(service,factory, frequency);

        var actual = subject.streamReorders();
        assertEquals(asList(expectedProductReorder), actual.blockFirst().data());

        verify(this.service).findAllProductReorders();
    }
}