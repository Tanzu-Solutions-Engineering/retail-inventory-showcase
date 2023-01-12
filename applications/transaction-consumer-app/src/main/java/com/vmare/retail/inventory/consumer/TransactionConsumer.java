package com.vmare.retail.inventory.consumer;

import com.vmware.retail.inventory.domain.pos.Transaction;
import com.vmware.retail.inventory.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

/**
 * @author gregory green
 */
@RequiredArgsConstructor
@Component
public class TransactionConsumer implements Consumer<Transaction> {

    private final TransactionService transactionService;

    @Override
    public void accept(Transaction transaction) {
        transactionService.saveTransaction(transaction);
    }
}
