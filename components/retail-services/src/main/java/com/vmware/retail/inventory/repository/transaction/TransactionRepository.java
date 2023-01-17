package com.vmware.retail.inventory.repository.transaction;

import com.vmware.retail.inventory.domain.pos.Transaction;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository {
    void save(Transaction transaction);
}
