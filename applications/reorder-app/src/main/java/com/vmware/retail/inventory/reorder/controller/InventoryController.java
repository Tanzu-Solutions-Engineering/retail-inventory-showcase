package com.vmware.retail.inventory.reorder.controller;


import com.vmware.retail.inventory.domain.StoreProductInventory;
import com.vmware.retail.inventory.repository.product.ProductReorderRepository;
import com.vmware.retail.inventory.repository.store.StoreProductInventoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;
import com.vmware.retail.inventory.domain.ProductReorder;
import java.time.Duration;
import java.util.concurrent.ThreadFactory;

@Slf4j
@RestController
@RequestMapping("inventory")
public class InventoryController {

    private final ProductReorderRepository repository;
    private final StoreProductInventoryRepository storeProductInventoryRepository;
    private final ThreadFactory factory;
    private final long refreshRateSecs;

    public InventoryController(ProductReorderRepository repository,
                               StoreProductInventoryRepository storeProductInventoryRepository,
                               ThreadFactory factory,
                               @Value("${retail.reorder.refresh.rateSeconds:5}")
                               long refreshRateSecs) {
        this.repository = repository;
        this.storeProductInventoryRepository = storeProductInventoryRepository;
        this.factory = factory;
        this.refreshRateSecs = refreshRateSecs;
    }

    @GetMapping("reorders")
    public Flux<ServerSentEvent<Iterable<ProductReorder>>> streamReorders() {

        Scheduler scheduler = Schedulers.newParallel(5,factory);
        return Flux.interval(Duration.ofSeconds(refreshRateSecs),scheduler)
                .map(sequence -> ServerSentEvent.<Iterable<ProductReorder>> builder()
                        .data(repository.findAll())
                        .build());
    }


    @GetMapping("products")
    public Flux<ServerSentEvent<Iterable<StoreProductInventory>>> streamProductStoreInventory() {

        Scheduler scheduler = Schedulers.newParallel(5,factory);
        return Flux.interval(Duration.ofSeconds(refreshRateSecs),scheduler)
                .map(sequence -> ServerSentEvent.<Iterable<StoreProductInventory>> builder()
                        .data(storeProductInventoryRepository.findAll())
                        .build());
    }

}
