package com.vmware.retail.inventory.repository.product.gemfire;

import com.vmware.retail.inventory.domain.ProductReorder;
import com.vmware.retail.inventory.repository.product.ProductReorderRepository;
import lombok.RequiredArgsConstructor;
import org.apache.geode.cache.Region;
import org.springframework.stereotype.Repository;

import java.util.function.Supplier;

@RequiredArgsConstructor
@Repository
public class ProductReorderGfRepository implements ProductReorderRepository {

    private final Supplier<Region<String, ProductReorder>> regionSupplier;
    @Override
    public Iterable<ProductReorder> findAll() {
        var region = regionSupplier.get();

        var maps= region.getAll(region.keySetOnServer());

        if(maps == null)
            return null;

        return maps.values();
    }

    @Override
    public void save(ProductReorder reorderPoint) {
        var region = regionSupplier.get();
        region.put(reorderPoint.getId(),reorderPoint);
    }
}
