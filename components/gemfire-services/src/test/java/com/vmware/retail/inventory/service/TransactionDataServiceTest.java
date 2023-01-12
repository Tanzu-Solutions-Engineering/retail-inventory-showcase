package com.vmware.retail.inventory.service;

import com.vmware.retail.inventory.domain.StoreProductInventory;
import com.vmware.retail.inventory.domain.pos.Transaction;
import com.vmware.retail.inventory.repository.store.StoreProductInventoryRepository;
import com.vmware.retail.inventory.repository.transaction.TransactionRepository;
import nyla.solutions.core.patterns.creational.generator.JavaBeanGeneratorCreator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionDataServiceTest {

   @Mock
   private TransactionRepository transactionRepository;

   @Mock
   private StoreProductInventoryRepository storeProductInventoryRepository;

   @Mock
   private TransactionDataService subject;
   private Transaction transaction;
    private StoreProductInventory storeProductInventory;

    @BeforeEach
    void setUp() {
        subject = new TransactionDataService(transactionRepository,storeProductInventoryRepository);
        transaction = JavaBeanGeneratorCreator.of(Transaction.class).create();
        storeProductInventory = JavaBeanGeneratorCreator.of(StoreProductInventory.class).create();
    }

    @Test
   void given_Transaction_when_save_then_when_save_then_saveTransaction_and_updateInventory() {

        int preCount = 10;
        storeProductInventory.setCurrentAvailable(preCount);

        when(storeProductInventoryRepository.findById(anyString())).thenReturn(Optional.of(storeProductInventory));

       subject.saveTransaction(transaction);

       verify(this.transactionRepository).save(any(Transaction.class));
        verify(this.storeProductInventoryRepository).save(any(StoreProductInventory.class));

        assertEquals(preCount-1, storeProductInventory.getCurrentAvailable());
   }

    @Test
    void given_transaction_when_toId_then_return_Id() {
        String expectedId = transaction.storeId()+ "|"+ transaction.itemId();
        assertEquals(expectedId, subject.toStoreProductId(transaction));
    }
}