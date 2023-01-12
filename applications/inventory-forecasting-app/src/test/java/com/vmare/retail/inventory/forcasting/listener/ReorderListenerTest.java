package com.vmare.retail.inventory.forcasting.listener;

import com.vmware.retail.inventory.domain.StoreProductInventory;
import com.vmware.retail.inventory.repository.product.ProductReorderRepository;
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
    private ProductReorderRepository repository;
    private StoreProductInventory storeProductInventory = JavaBeanGeneratorCreator.of(StoreProductInventory.class).create();
    @Mock
    private Operation baseOperation;

    @BeforeEach
    void setUp() {
        subject = new ReorderListener(repository);
    }

    @Test
    void given_event_when_check_then_createReorder() {

        when(cqEvent.getNewValue()).thenReturn(storeProductInventory);
        when(cqEvent.getBaseOperation()).thenReturn(baseOperation);
        when(baseOperation.isDestroy()).thenReturn(false);

        subject = new ReorderListener(repository);

        subject.check(cqEvent);

        verify(repository).save(any());
    }

    @Test
    void given_destroy_Event_when_check_then_doNot_CreateReorder() {


        when(cqEvent.getBaseOperation()).thenReturn(baseOperation);
        when(baseOperation.isDestroy()).thenReturn(true);

        subject = new ReorderListener(repository);

        subject.check(cqEvent);

        verify(repository,never()).save(any());
    }
}