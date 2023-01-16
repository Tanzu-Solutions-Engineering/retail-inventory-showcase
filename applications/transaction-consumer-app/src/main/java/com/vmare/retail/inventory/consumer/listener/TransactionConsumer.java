package com.vmare.retail.inventory.consumer.listener;

import com.vmware.retail.inventory.domain.pos.POSTransaction;
import com.vmware.retail.inventory.service.transaction.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

/**
 * @author gregory green
 */
@RequiredArgsConstructor
@Component
public class TransactionConsumer implements Consumer<POSTransaction> {
    @Override
    public void accept(POSTransaction transaction) {

        transactionService.saveTransaction(transaction);
    }

    private final TransactionService transactionService;
}
