package com.vmware.retail.inventory.repository.transaction.gemfire;

import com.vmware.retail.inventory.domain.pos.Transaction;
import com.vmware.retail.inventory.repository.transaction.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.apache.geode.cache.Region;
import org.springframework.stereotype.Repository;

import java.util.function.Supplier;

@RequiredArgsConstructor
@Repository
public class TransactionGfRepository implements TransactionRepository {

    private final Supplier<Region<String, Transaction>> supplier;
    @Override
    public void save(Transaction transaction) {

        var region = supplier.get();

        region.put(transaction.getId(),transaction);
    }
}
