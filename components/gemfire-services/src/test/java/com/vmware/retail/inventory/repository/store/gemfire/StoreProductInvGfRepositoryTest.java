package com.vmware.retail.inventory.repository.store.gemfire;

import com.vmware.retail.inventory.domain.StoreProductInventory;
import nyla.solutions.core.patterns.creational.generator.JavaBeanGeneratorCreator;
import org.apache.geode.cache.Region;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.function.Supplier;

import static java.util.Arrays.asList;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Testing for StoreProductInvGfRepository
 * @author gregory green
 */
@ExtendWith(MockitoExtension.class)
class StoreProductInvGfRepositoryTest {
    @Mock
    private Region<String,StoreProductInventory> region;
    @Mock
    private StoreProductInvGfRepository subject;
    private final StoreProductInventory expectedStoreProductInventory = JavaBeanGeneratorCreator.of(StoreProductInventory.class).create();
    private Supplier<Region<String, StoreProductInventory>> supplier = () -> { return region;};

    @BeforeEach
    void setUp() {
        subject = new StoreProductInvGfRepository(supplier);
    }

    @Test
    void given_StoreProductInventory_when_save_then_save() {

        subject.save(expectedStoreProductInventory);

        verify(this.region).put(anyString(),any());
    }

    @Test
    void given_emptyStoreInventory_when_Find_then_ReturnEmptyOptional() {

        when(region.get(anyString())).thenReturn(null);

        var actual = subject.findById("121");

        assertThat(actual).isEqualTo(Optional.empty());

    }

    @Test
        void given_StoreProductInventory_when_find_then_when_read() {

            when(region.get(anyString())).thenReturn(expectedStoreProductInventory);

            subject = new StoreProductInvGfRepository(supplier);

            var actual = subject.findById(expectedStoreProductInventory.getId());
            assertEquals(expectedStoreProductInventory, actual.get());
        }


    @Test
    void findAll() {
        Iterable<StoreProductInventory> expected = asList(expectedStoreProductInventory);
        var actual = subject.findAll();

        assertEquals(expected, actual);
    }
}