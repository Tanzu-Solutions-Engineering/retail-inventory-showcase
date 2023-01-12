package com.vmware.retail.inventory.service;

import com.vmware.retail.inventory.domain.pos.Transaction;
import com.vmware.retail.inventory.repository.store.StoreProductInventoryRepository;
import com.vmware.retail.inventory.repository.transaction.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author gregory green
 */
@Service
@RequiredArgsConstructor
public class TransactionDataService implements TransactionService{
    private final TransactionRepository transactionRepository;
    private final StoreProductInventoryRepository storeProductInventoryRepository;

    @Override
    public void saveTransaction(Transaction transaction) {
        transactionRepository.save(transaction);

        var storeProductId = toStoreProductId(transaction);
        var inventoryResults = storeProductInventoryRepository.findById(storeProductId);

        if(inventoryResults.isEmpty())
            return;

        var inventory = inventoryResults.get();

        //Minus inventory
        var currentAvailability = inventory.getCurrentAvailable();
        if(currentAvailability > 0 )
            currentAvailability--;

        inventory.setCurrentAvailable(currentAvailability);
        storeProductInventoryRepository.save(inventory);
    }

    protected String toStoreProductId(Transaction transaction) {
        return new StringBuilder().append(
                transaction.storeId())
                .append("|")
                .append(transaction.itemId()).toString();
    }

}
