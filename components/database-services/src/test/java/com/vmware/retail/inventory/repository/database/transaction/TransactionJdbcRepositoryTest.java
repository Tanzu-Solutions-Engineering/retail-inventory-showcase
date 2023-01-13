package com.vmware.retail.inventory.repository.database.transaction;

import com.vmware.retail.inventory.domain.pos.Transaction;
import nyla.solutions.core.patterns.creational.generator.JavaBeanGeneratorCreator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TransactionJdbcRepositoryTest {

        @Mock
        private NamedParameterJdbcTemplate template;

        @Mock
        private TransactionJdbcRepository subject;

        private Transaction expected = JavaBeanGeneratorCreator.of(Transaction.class).create();

        @Test
        void given_JdbcTemplate_when_save_then_when_save_then_repository_saves() {


            subject = new TransactionJdbcRepository(template);

            subject.save(expected);

            verify(this.template).update(anyString(),any(Map.class));
        }
}