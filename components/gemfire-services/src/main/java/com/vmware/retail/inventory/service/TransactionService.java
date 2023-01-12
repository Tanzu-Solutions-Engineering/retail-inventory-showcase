package com.vmware.retail.inventory.service;

import com.vmware.retail.inventory.domain.pos.Transaction;

public interface TransactionService {
    void saveTransaction(Transaction transaction);

}
