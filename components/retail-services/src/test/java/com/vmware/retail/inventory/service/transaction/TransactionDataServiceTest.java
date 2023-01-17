package com.vmware.retail.inventory.service.transaction;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.vmware.retail.inventory.domain.StoreProductInventory;
import com.vmware.retail.inventory.domain.pos.POSTransaction;
import com.vmware.retail.inventory.repository.store.StoreProductInventoryRepository;
import com.vmware.retail.inventory.repository.transaction.TransactionRepository;
import com.vmware.retail.inventory.service.transaction.TransactionDataService;
import nyla.solutions.core.patterns.creational.generator.JavaBeanGeneratorCreator;
import nyla.solutions.core.patterns.integration.Publisher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.text.SimpleDateFormat;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionDataServiceTest {

   @Mock
   private TransactionRepository transactionRepository;

   @Mock
   private StoreProductInventoryRepository storeProductInventoryRepository;

   @Mock
   private TransactionDataService subject;
   private POSTransaction transaction;
    private StoreProductInventory storeProductInventory;
    private ObjectMapper objectMapper;

    @Mock
    private Publisher<StoreProductInventory> publisher;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy hh:mm:sss");
        objectMapper.setDateFormat(df);

        subject = new TransactionDataService(transactionRepository,storeProductInventoryRepository, publisher);
        transaction = JavaBeanGeneratorCreator.of(POSTransaction.class).create();
        storeProductInventory = JavaBeanGeneratorCreator.of(StoreProductInventory.class).create();
    }

    @Test
   void given_Transaction_when_save_then_when_save_then_saveTransaction_and_updateInventory() throws JsonProcessingException {

        var json = objectMapper.writeValueAsString(transaction);
        System.out.println(json);

        int preCount = 10;
        storeProductInventory.setCurrentAvailable(preCount);

        when(storeProductInventoryRepository.findById(anyString())).thenReturn(Optional.of(storeProductInventory));

       subject.saveTransaction(transaction);

       verify(this.transactionRepository).save(any());
        verify(this.publisher).send(any(StoreProductInventory.class));

        assertEquals(preCount-1, storeProductInventory.getCurrentAvailable());
   }


    @Test
    void given_Transaction_with_noInventory_when_save_then_saveTransaction() {

        when(this.storeProductInventoryRepository.findById(anyString())).thenReturn(null);

        subject.saveTransaction(transaction);


        verify(this.transactionRepository).save(any());
        verify(this.storeProductInventoryRepository,never()).save(any(StoreProductInventory.class));

    }

    @Test
    void given_transaction_when_toId_then_return_Id() {
        String expectedId = transaction.itemId() +"|"+ transaction.storeId();
        assertEquals(expectedId, subject.toStoreProductId(transaction));
    }

    @Test
    void toTransactionId() {
        POSTransaction pos = JavaBeanGeneratorCreator.of(POSTransaction.class).create();
        var actual = subject.toTransactionId(pos);

        assertThat(actual).startsWith(pos.itemId());
    }
}