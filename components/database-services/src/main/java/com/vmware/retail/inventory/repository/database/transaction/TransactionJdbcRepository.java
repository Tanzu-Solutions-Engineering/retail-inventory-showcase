package com.vmware.retail.inventory.repository.database.transaction;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vmware.retail.inventory.domain.pos.Transaction;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Map;

import static nyla.solutions.core.util.Organizer.toMap;

@Repository
@RequiredArgsConstructor
@Slf4j
public class TransactionJdbcRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final ObjectMapper objectMapper;

    @SneakyThrows
    public void save(Transaction transaction) {
        final var upsert = """
                INSERT INTO pos_transactions(id, data, created_ts) 
                VALUES (:id,to_json(:data::json),:created_ts);
                """;

        log.info("sql {} inputs: {}",upsert,transaction);

        namedParameterJdbcTemplate.update(upsert,
                constructInputMap(transaction));
    }

    @SneakyThrows
    protected Map<String,Object> constructInputMap(Transaction transaction) {

        return toMap("id",transaction.getId(),
                "data", objectMapper.writeValueAsString(transaction),
                "created_ts",transaction.getCreateDateTime()
        );
    }
}
