package com.vmware.retail.inventory.repository.store.gemfire;

import com.vmware.retail.inventory.domain.StoreProductInventory;
import com.vmware.retail.inventory.repository.store.StoreProductInventoryRepository;
import lombok.RequiredArgsConstructor;
import org.apache.geode.cache.Region;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * GemFire repository implement for store product information
 * @author gregory green
 */
@RequiredArgsConstructor
@Repository
public class StoreProductInvGfRepository implements StoreProductInventoryRepository {

    private final Supplier<Region<String, StoreProductInventory>> supplier;

    @Override
    public Optional<StoreProductInventory> findById(String storeProductId) {
        var region = supplier.get();

        var storeProductInventory = region.get(storeProductId);

        if(storeProductInventory == null)
            return Optional.empty();

        return Optional.of(storeProductInventory);
    }

    @Override
    public void save(StoreProductInventory inventory) {

        var region = supplier.get();

        region.put(inventory.getId(),inventory);
    }

    @Override
    public Iterable<StoreProductInventory> findAll() {

        var region = supplier.get();

        Map<String,StoreProductInventory> map =  region.getAll(region.keySetOnServer());

        return map.values();
    }
}
