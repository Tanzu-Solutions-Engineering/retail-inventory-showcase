package com.vmware.retail.inventory.repository.database.store.product;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vmware.retail.inventory.domain.StoreProductInventory;
import com.vmware.retail.inventory.repository.store.StoreProductInventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static nyla.solutions.core.util.Organizer.toMap;

@Repository
@RequiredArgsConstructor
@Slf4j
public class StoreProductInventoryJdbcRepository implements StoreProductInventoryRepository {

    private final NamedParameterJdbcTemplate template;
    private final ObjectMapper objectMapper;

    @Override
    public Optional<StoreProductInventory> findById(String storeProductId) {

        String sql = """
                select data 
                from pos_store_product_inv
                where id = :id
                """;

        Map<String, ?> map = toMap("id",storeProductId);
        RowMapper<StoreProductInventory> rowMapper = (resultSet,i) ->
        {
            try {
                return objectMapper.readValue(resultSet.getString(1),StoreProductInventory.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        };

        log.info("id: {},  SQL: {}",storeProductId,sql);

        List<StoreProductInventory> storeProductInventories = template.query(sql,map,rowMapper);

        log.info("storeProductInventories",storeProductInventories);

        if(storeProductInventories == null || storeProductInventories.isEmpty())
            return Optional.empty();

        return Optional.of(storeProductInventories.iterator().next());
    }

    @SneakyThrows
    @Override
    public void save(StoreProductInventory inventory) {

        log.info("Saving: {} ",inventory);
        var sql = """
                insert into pos_store_product_inv(id,data,created_ts)
                values(:id,to_json(:data::json),:created_ts)
                ON CONFLICT (id)
                DO
                   UPDATE SET data = to_json(:data::json);
                ;
                """;

        log.info("SQL: {}",sql);

        LocalDateTime now = LocalDateTime.now();
        template.update(sql,
                toMap(
                        "id",inventory.getId(),
                        "data",objectMapper.writeValueAsString(inventory),
                        "created_ts",now
                        ));

    }

    @Override
    public Iterable<StoreProductInventory> findAll() {

        String sql = """
                select data 
                from pos_store_product_inv
                """;

        RowMapper<StoreProductInventory> rowMapper = (resultSet,i) ->
        {
            try {
                return objectMapper.readValue(resultSet.getString(1),StoreProductInventory.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        };

        log.info(" SQL: {}",sql);

        List<StoreProductInventory> storeProductInventories = template.query(sql,rowMapper);

        log.info("storeProductInventories",storeProductInventories);

        return storeProductInventories;

    }
}
