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


     -- LEAD TIME

     SELECT
         avg(day_orders_cnt)::INTEGER avg_daily_orders,
         max(lead_time_days)::INTEGER max_lead_time_days
     FROM
         (select count(*) day_orders_cnt, max(lead_time_days) lead_time_days, date_trunc('day', created_ts)
         from  pos_orders
         WHERE store_id = '001' and
         product_id = 'SKU-PEANUT-BUTTER'
         GROUP BY  date_trunc('day', created_ts))
     pos_orders_counts;

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

        int avgDailySales = 0;
        Object results = sales_results.get("avg_sales");
        if(results != null)
            avgDailySales = (Integer)results;


        int maxDailySales = 0;
        results = sales_results.get("max_sales");
        if(results != null)
            maxDailySales = (Integer)results;

        int avgDailyOrders = 3 ;
        int maxDailyOrders = 3 ;
        int maxLeadTimeDays = 5;
        int avgLeadTimeDays = 5;

        var leadSQL = """
                SELECT 
                    avg(day_orders_cnt)::INTEGER avg_daily_orders,
                    max(lead_time_days)::INTEGER max_daily_orders,
                    avg(lead_time_days)::INTEGER avg_lead_time_days,
                    max(lead_time_days)::INTEGER max_lead_time_days
                FROM    
                (select count(*) day_orders_cnt, max(lead_time_days) lead_time_days, date_trunc('day', created_ts)
                from  pos_orders
                WHERE store_id = :storeId and
                    product_id = :productId
                GROUP BY  date_trunc('day', created_ts))
                pos_orders_counts;
                """;

        Map<String,Object> leadTimeResults = this.template.queryForMap(leadSQL,inputMap);


        if(leadTimeResults != null && !leadTimeResults.isEmpty())
        {
             results = leadTimeResults.get("max_lead_time_days");
            if(results != null)
                maxLeadTimeDays = (Integer)results;

            results = leadTimeResults.get("avg_lead_time_days");
            if(results != null)
                avgLeadTimeDays = (Integer)results;

            results = leadTimeResults.get("avg_daily_orders");
            if(results != null)
                avgDailyOrders = (Integer)results;


            results = leadTimeResults.get("max_daily_orders");
            if(results != null)
                maxDailyOrders = (Integer)results;

        }

        return ProductReorderModelPrediction
                .builder()
                .averageDailySales(avgDailySales)
                .maxDailyOrders(maxDailyOrders)
                .maxDailySales(maxDailySales)
                .averageDailyOrders(avgDailyOrders)
                .maxLeadTimeDays(maxLeadTimeDays)
                .averageLeadTimeDays(avgLeadTimeDays)
                .storeId(inventory.getStoreId())
                .productId(inventory.getProductId())
                .id(inventory.getId()).build();
    }
}
