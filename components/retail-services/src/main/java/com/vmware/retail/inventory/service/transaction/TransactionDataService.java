package com.vmware.retail.inventory.service.transaction;

import ch.qos.logback.core.net.server.ConcurrentServerRunner;
import com.vmware.retail.inventory.domain.StoreProductInventory;
import com.vmware.retail.inventory.domain.pos.POSTransaction;
import com.vmware.retail.inventory.domain.pos.Transaction;
import com.vmware.retail.inventory.repository.store.StoreProductInventoryRepository;
import com.vmware.retail.inventory.repository.transaction.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nyla.solutions.core.patterns.integration.Publisher;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

import java.time.LocalDateTime;

import static nyla.solutions.core.util.Text.generateId;

/**
 * @author gregory green
 */
@RequiredArgsConstructor
@Component
@Slf4j
public class TransactionDataService implements TransactionService{

    private final TransactionRepository transactionRepository;
    private final StoreProductInventoryRepository storeProductInventoryRepository;
    private final Publisher<StoreProductInventory> storeProducerInventoryPublisher;

    @Transactional
    public void saveTransaction(POSTransaction pos) {

        log.info("Saving POSTransaction: {}",pos);

        var timestamp = pos.timestamp();

        if(timestamp == null)
            timestamp = LocalDateTime.now();

        var transaction =  Transaction.builder()
                .posTransaction(pos)
                .id(toTransactionId(pos))
                .createDateTime(timestamp)
                .build();


        log.info("Saving transaction: {}",transaction);
        transactionRepository.save(transaction);

        var storeProductId = toStoreProductId(pos);
        var inventoryResults = storeProductInventoryRepository.findById(storeProductId);

        if(inventoryResults == null || inventoryResults.isEmpty())
            return;

        var inventory = inventoryResults.get();

        //Minus inventory
        var currentAvailability = inventory.getCurrentAvailable();
        if(currentAvailability > 0 )
            currentAvailability--;


        inventory.setCurrentAvailable(currentAvailability);

        storeProducerInventoryPublisher.send(inventory);

        //TODO: storeProductInventoryRepository.save(inventory);
    }

    protected String toTransactionId(POSTransaction pos) {
        return new StringBuilder().append(pos.itemId()).append("|")
                .append(pos.storeId())
                .append(generateId()).toString();
    }

    protected String toStoreProductId(POSTransaction transaction) {
        return new StringBuilder().append(transaction.itemId())
                .append("|")
                .append(transaction.storeId()).toString();
    }


}
