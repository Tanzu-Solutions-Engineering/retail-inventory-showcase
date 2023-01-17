package com.vmware.retail.inventory.repository.writer;

import com.vmware.retail.inventory.domain.StoreProductInventory;
import com.vmware.retail.inventory.repository.store.StoreProductInventoryRepository;
import lombok.RequiredArgsConstructor;
import org.apache.geode.cache.CacheWriterException;
import org.apache.geode.cache.EntryEvent;
import org.apache.geode.cache.util.CacheWriterAdapter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StoreProductInventoryCacheWriter extends CacheWriterAdapter {

    private final StoreProductInventoryRepository repository; //should be JDBC

    @Override
    public void beforeCreate(EntryEvent event) throws CacheWriterException {
        repository.save((StoreProductInventory) event.getNewValue());
    }

    @Override
    public void beforeUpdate(EntryEvent event) throws CacheWriterException {
        repository.save((StoreProductInventory) event.getNewValue());
    }
}
