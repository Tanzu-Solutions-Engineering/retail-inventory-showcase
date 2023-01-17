package com.vmware.retail.inventory.repository.database.store.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vmware.retail.inventory.domain.StoreProductInventory;
import nyla.solutions.core.patterns.creational.generator.JavaBeanGeneratorCreator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.Collections;
import java.util.Map;

import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StoreProductInventoryJdbcRepositoryTest {

    private StoreProductInventoryJdbcRepository subject;

    @Mock
    private NamedParameterJdbcTemplate template;

    private StoreProductInventory storeProductInventory = JavaBeanGeneratorCreator.of(StoreProductInventory.class).create();
    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        subject = new StoreProductInventoryJdbcRepository(template,objectMapper);
    }

    @Test
    void findById() {
        when(template.query(anyString(),any(Map.class),any(RowMapper.class))).thenReturn(singletonList(storeProductInventory));

       var actual = subject.findById(storeProductInventory.getId());
       assertEquals(storeProductInventory, actual.get());
    }

    @Test
    void save() {

        subject.save(storeProductInventory);

        verify(template).update(anyString(),any(Map.class));

    }
}