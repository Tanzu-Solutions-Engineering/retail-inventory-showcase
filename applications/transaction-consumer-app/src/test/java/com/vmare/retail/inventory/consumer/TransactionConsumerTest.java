package com.vmare.retail.inventory.consumer;

import com.vmare.retail.inventory.consumer.listener.TransactionConsumer;
import com.vmware.retail.inventory.domain.pos.POSTransaction;
import com.vmware.retail.inventory.service.transaction.TransactionService;
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
    private POSTransaction expected = JavaBeanGeneratorCreator.of(POSTransaction.class).create();

    @Test
    void given__when_save_then_when_save_then_repository_saves() {


        subject = new TransactionConsumer(service);

        subject.accept(expected);

        verify(this.service).saveTransaction(any(POSTransaction.class));

    }
}