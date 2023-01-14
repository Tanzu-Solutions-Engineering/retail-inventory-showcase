package com.vmware.retail.inventory.repository.transaction.gemfire;

import com.vmware.retail.inventory.domain.pos.Transaction;
import nyla.solutions.core.patterns.creational.generator.JavaBeanGeneratorCreator;
import org.apache.geode.cache.Region;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TransactionGfRepositoryTest {

    @Mock
    private Region<String, Transaction> region;
    private Supplier<Region<String, Transaction>> supplier = () -> {return region;};
    private TransactionGfRepository subject;
    private Transaction transaction = JavaBeanGeneratorCreator.of(Transaction.class).create();

    @Test
    void save() {
        subject = new TransactionGfRepository(supplier);

        subject.save(transaction);

        verify(region).put(anyString(),any());
    }
}