package com.vmware.retail.inventory.service.transaction;

import com.vmware.retail.inventory.domain.pos.POSTransaction;


public interface TransactionService {
    void saveTransaction(POSTransaction transaction);

}
