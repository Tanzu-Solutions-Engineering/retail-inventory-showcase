package com.vmare.retail.inventory.consumer;

import com.vmware.retail.inventory.domain.pos.Transaction;
import com.vmware.retail.inventory.service.TransactionService;
import nyla.solutions.core.patterns.creational.generator.JavaBeanGeneratorCreator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TransactionConsumerTest {

    @Mock
    private TransactionService service;

    @Mock
    private TransactionConsumer subject;
    private Transaction expected = JavaBeanGeneratorCreator.of(Transaction.class).create();

    @Test
    void given__when_save_then_when_save_then_repository_saves() {


        subject = new TransactionConsumer(service);

        subject.accept(expected);

        verify(this.service).saveTransaction(any(Transaction.class));

    }
}