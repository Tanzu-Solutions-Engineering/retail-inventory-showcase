package com.vmare.retail.inventory.forcasting.repository.product.training.jdbc;

import com.vmare.retail.inventory.forcasting.repository.product.training.ReorderTrainingRepository;
import com.vmware.retail.inventory.domain.StoreProductInventory;
import com.vmware.retail.inventory.ml.model.ProductReorderModelPrediction;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Map;

import static nyla.solutions.core.util.Organizer.toMap;

@Repository
@RequiredArgsConstructor
public class ReorderTrainingJdbcRepository implements ReorderTrainingRepository {

    private final NamedParameterJdbcTemplate template;

    /**

     SELECT
         max(day_sales_cnt)::INTEGER max_sales,
         avg(day_sales_cnt)::INTEGER avg_sales
     FROM
         (SELECT count(*) day_sales_cnt,
         data->'posTransaction'->>'itemId' productId,
         date_trunc('day', created_ts) create_date
         FROM pos_transactions
     WHERE
         data->'posTransaction'->>'storeId' = '001' and
         data->'posTransaction'->>'itemId' = 'SKU-PEANUT-BUTTER'
         group by data->'posTransaction'->>'itemId',
         date_trunc('day', created_ts)
         ) daily_product_sales;


     * @param inventory
     * @return
     */
    @Override
    public ProductReorderModelPrediction train(StoreProductInventory inventory) {

        var pos_transaction_sql = """
                SELECT 
                    max(day_sales_cnt)::INTEGER max_sales,
                    avg(day_sales_cnt)::INTEGER avg_sales
                FROM    
                (SELECT count(*) day_sales_cnt, 
                    data->'posTransaction'->>'itemId' productId,
                    date_trunc('day', created_ts) create_date 
                FROM pos_transactions
                WHERE  
                        data->'posTransaction'->>'storeId' = :storeId and 
                        data->'posTransaction'->>'itemId' = :productId
                 group by data->'posTransaction'->>'itemId',
                  date_trunc('day', created_ts)
                  ) daily_product_sales;
                """;


        Map<String, ?> inputMap = toMap(
                "storeId",inventory.getStoreId(),
                        "productId",inventory.getProductId());

        Map<String,?> sales_results = this.template.queryForMap(pos_transaction_sql,inputMap);

        int avgDailySales = (Integer)sales_results.get("avg_sales");
        int maxDailySales = (Integer)sales_results.get("max_sales");

        int avgDailyOrders = -1 ; //TODO
        int maxLeadTimeDays = -1; //TODO

        return ProductReorderModelPrediction
                .builder()
                .averageDailySales(avgDailySales)
                .maxDailySales(maxDailySales)
                .averageDailyOrders(avgDailyOrders)
                .maxLeadTimeDays(maxLeadTimeDays)
                .storeId(inventory.getStoreId())
                .productId(inventory.getProductId())
                .id(inventory.getId()).build();
    }
}
