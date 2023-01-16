package com.vmware.retail.inventory.repository.database.transaction;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.vmware.retail.inventory.domain.pos.Transaction;
import nyla.solutions.core.patterns.creational.generator.JavaBeanGeneratorCreator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.time.LocalDateTime;
import java.util.Map;

import static nyla.solutions.core.util.Organizer.toMap;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TransactionJdbcRepositoryTest {

        @Mock
        private NamedParameterJdbcTemplate template;

        @Mock
        private TransactionJdbcRepository subject;

        private Transaction transaction = JavaBeanGeneratorCreator.of(Transaction.class).create();
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        subject = new TransactionJdbcRepository(template,objectMapper);
    }

    @Test
        void given_JdbcTemplate_when_save_then_when_save_then_repository_saves() {




            subject.save(transaction);

            verify(this.template).update(anyString(),any(Map.class));
        }

        @DisplayName("Given transaction when constructInputMap then return map")
    @Test
    void constructMap() throws JsonProcessingException {

        assertNotNull(transaction.getCreateDateTime());

            String json = objectMapper.writeValueAsString(transaction);
            var expectedMap = toMap("data",json,
                    "id",transaction.getId(),
                   "created_ts", transaction.getCreateDateTime());
            var actual = subject.constructInputMap(transaction);

            assertEquals(expectedMap, actual);
    }
}