package com.vmare.retail.inventory.forcasting.listener;

import com.vmare.retail.inventory.forcasting.repository.ProductReorderRepository;
import com.vmware.retail.inventory.domain.StoreProductInventory;
import nyla.solutions.core.patterns.creational.generator.JavaBeanGeneratorCreator;
import org.apache.geode.cache.query.CqEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReorderListenerTest {

    private ReorderListener subject;

    @Mock
    private CqEvent cqEvent;
    @Mock
    private ProductReorderRepository repository;
    private StoreProductInventory storeProductInventory = JavaBeanGeneratorCreator.of(StoreProductInventory.class).create();

    @BeforeEach
    void setUp() {
        subject = new ReorderListener(repository);
    }

    @Test
    void given_event_when_check_then_createReorder() {

        when(cqEvent.getNewValue()).thenReturn(storeProductInventory);

        subject = new ReorderListener(repository);

        subject.check(cqEvent);

        verify(repository).save(any());
    }
}