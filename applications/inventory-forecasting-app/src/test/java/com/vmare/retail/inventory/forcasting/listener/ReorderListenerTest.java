package com.vmare.retail.inventory.forcasting.listener;

import com.vmare.retail.inventory.forcasting.service.ProductReorderService;
import com.vmware.retail.inventory.domain.StoreProductInventory;
import nyla.solutions.core.patterns.creational.generator.JavaBeanGeneratorCreator;
import org.apache.geode.cache.Operation;
import org.apache.geode.cache.query.CqEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReorderListenerTest {

    private ReorderListener subject;

    @Mock
    private CqEvent cqEvent;
    @Mock
    private ProductReorderService service;
    private StoreProductInventory storeProductInventory = JavaBeanGeneratorCreator.of(StoreProductInventory.class).create();
    @Mock
    private Operation baseOperation;
    @Mock
    private Operation queryOperation;

    @BeforeEach
    void setUp() {
        subject = new ReorderListener(service);
    }

    @Test
    void given_event_when_check_then_createReorder() {

        when(cqEvent.getNewValue()).thenReturn(storeProductInventory);
        when(cqEvent.getBaseOperation()).thenReturn(baseOperation);
        when(cqEvent.getQueryOperation()).thenReturn(queryOperation);
        when(baseOperation.isDestroy()).thenReturn(false);
        when(queryOperation.isDestroy()).thenReturn(false);


        subject.check(cqEvent);

        verify(service).submitReorder(any(StoreProductInventory.class));
    }

    @Test
    void given_destroy_Event_when_check_then_doNot_CreateReorder() {


        when(cqEvent.getBaseOperation()).thenReturn(baseOperation);
        when(baseOperation.isDestroy()).thenReturn(true);

        subject = new ReorderListener(service);

        subject.check(cqEvent);

        verify(service,never()).submitReorder(any());
    }
}