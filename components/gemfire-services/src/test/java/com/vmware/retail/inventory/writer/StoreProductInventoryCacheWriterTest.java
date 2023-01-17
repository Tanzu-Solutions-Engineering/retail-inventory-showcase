package com.vmware.retail.inventory.writer;

import com.vmware.retail.inventory.domain.StoreProductInventory;
import com.vmware.retail.inventory.repository.store.StoreProductInventoryRepository;
import com.vmware.retail.inventory.repository.writer.StoreProductInventoryCacheWriter;
import nyla.solutions.core.patterns.creational.generator.JavaBeanGeneratorCreator;
import org.apache.geode.cache.EntryEvent;
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
class StoreProductInventoryCacheWriterTest {

    private StoreProductInventoryCacheWriter subject;

    @Mock
    private StoreProductInventoryRepository repository;

    @Mock
    private EntryEvent event;
    private StoreProductInventory storeProductInventory = JavaBeanGeneratorCreator.of(StoreProductInventory.class).create();

    @BeforeEach
    void setUp() {
        subject = new StoreProductInventoryCacheWriter(repository);
    }

    @DisplayName("Given storeProductInventory when beforeCreate then save to databases")
    @Test
    void beforeCreated() {

        when(event.getNewValue()).thenReturn(storeProductInventory);

        subject.beforeCreate(event);

        verify(repository).save(any(StoreProductInventory.class));
    }


    @DisplayName("Given storeProductInventory when beforeCreate then save to databases")
    @Test
    void beforeUpdate() {

        when(event.getNewValue()).thenReturn(storeProductInventory);

        subject.beforeUpdate(event);

        verify(repository).save(any(StoreProductInventory.class));
    }
}