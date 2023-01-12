package com.vmare.retail.inventory.consumer;

import com.vmware.retail.inventory.domain.pos.POSTransaction;
import com.vmware.retail.inventory.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

/**
 * @author gregory green
 */
@RequiredArgsConstructor
@Component
public class TransactionConsumer implements Consumer<POSTransaction> {

    private final TransactionService transactionService;

    @Override
    public void accept(POSTransaction transaction) {
        transactionService.saveTransaction(transaction);
    }
}
