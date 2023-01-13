package com.vmare.retail.inventory.consumer.writer;

import com.vmware.retail.inventory.domain.pos.Transaction;
import com.vmware.retail.inventory.repository.database.transaction.TransactionJdbcRepository;
import nyla.solutions.core.patterns.creational.generator.JavaBeanGeneratorCreator;
import org.apache.geode.cache.EntryEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionDbCacheWriterTest {

    @Mock
    private EntryEvent<String, Transaction> entryEvent;



    @Mock
    private TransactionJdbcRepository repository;

    @Mock
    private TransactionDbCacheWriter subject;
    private Transaction transaction = JavaBeanGeneratorCreator.of(Transaction.class).create();

    @Test
    void given_TransactionJdbcRepository_when_save_then_when_save_then_repository_saves() {

        when(entryEvent.getNewValue()).thenReturn(transaction);
        subject = new TransactionDbCacheWriter(repository);

        subject.beforeCreate(entryEvent);

        verify(this.repository).save(any(Transaction.class));
    }
}