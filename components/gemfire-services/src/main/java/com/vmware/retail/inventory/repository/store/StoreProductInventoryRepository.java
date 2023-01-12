package com.vmware.retail.inventory.repository.store;

import com.vmware.retail.inventory.domain.StoreProductInventory;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface StoreProductInventoryRepository extends CrudRepository<StoreProductInventory,String> {
}
