package com.vmare.retail.inventory.consumer.writer;

import com.vmware.retail.inventory.domain.pos.Transaction;
import com.vmware.retail.inventory.repository.database.transaction.TransactionJdbcRepository;
import lombok.RequiredArgsConstructor;
import org.apache.geode.cache.CacheWriterException;
import org.apache.geode.cache.EntryEvent;
import org.apache.geode.cache.Region;
import org.apache.geode.cache.util.CacheWriterAdapter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TransactionDbCacheWriter extends CacheWriterAdapter<String, Transaction> {
    private final TransactionJdbcRepository repository;

    @Override
    public void beforeCreate(EntryEvent<String, Transaction> event) throws CacheWriterException
    {
        this.repository.save(event.getNewValue());
    }

    @Override
    public void beforeUpdate(EntryEvent<String, Transaction> event) throws CacheWriterException {
        this.repository.save(event.getNewValue());
    }

    private void save(Transaction region) {
        this.repository.save(region);
    }
}
