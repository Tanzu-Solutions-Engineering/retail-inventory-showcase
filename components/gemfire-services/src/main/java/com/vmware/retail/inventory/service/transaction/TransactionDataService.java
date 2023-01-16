package com.vmware.retail.inventory.service.transaction;

import com.vmware.retail.inventory.domain.pos.POSTransaction;
import com.vmware.retail.inventory.domain.pos.Transaction;
import com.vmware.retail.inventory.repository.store.StoreProductInventoryRepository;
import com.vmware.retail.inventory.repository.transaction.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

import static nyla.solutions.core.util.Text.generateId;

/**
 * @author gregory green
 */
@RequiredArgsConstructor
@Component
public class TransactionDataService implements TransactionService{

    @Transactional
    public void saveTransaction(POSTransaction pos) {


        var transaction =  Transaction.builder().posTransaction(pos).id(toTransactionId(pos)).build();
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
        storeProductInventoryRepository.save(inventory);
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

    private final TransactionRepository transactionRepository;
    private final StoreProductInventoryRepository storeProductInventoryRepository;
}
