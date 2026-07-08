package com.example.crudsbmsapp.service;

import com.example.crudsbmsapp.config.FeatureFlagProperties;
import com.example.crudsbmsapp.dto.ItemRequest;
import com.example.crudsbmsapp.dto.ItemResponse;
import com.example.crudsbmsapp.repository.ItemRepository;
import com.example.crudsbmsapp.repository.PostgresItemRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

@Service
public class ItemService {
    private final ItemRepository itemRepository;
    private final PostgresItemRepository postgresItemRepository;
    private final FeatureFlagProperties featureFlagProperties;

    public ItemService(ItemRepository itemRepository,
                       PostgresItemRepository postgresItemRepository,
                       FeatureFlagProperties featureFlagProperties) {
        this.itemRepository = itemRepository;
        this.postgresItemRepository = postgresItemRepository;
        this.featureFlagProperties = featureFlagProperties;
    }

    @Async
    @Transactional
    public CompletableFuture<ItemResponse> createItem(ItemRequest request) {
        if (!featureFlagProperties.isFeatureFlag()) {
            return CompletableFuture.supplyAsync(() -> {
                try {
                    ItemResponse h2Result = itemRepository.create(request.name(), request.description());
                    ItemResponse postgresResult = postgresItemRepository.create(request.name(), request.description());
                    return h2Result;
                } catch (RuntimeException ex) {
                    itemRepository.deleteByName(request.name());
                    throw ex;
                }
            });
        }
        return CompletableFuture.supplyAsync(() -> postgresItemRepository.create(request.name(), request.description()));
    }

    @Async
    public CompletableFuture<Optional<ItemResponse>> getItem(Long id) {
        if (!featureFlagProperties.isFeatureFlag()) {
            return CompletableFuture.supplyAsync(() -> itemRepository.findById(id));
        }
        return CompletableFuture.supplyAsync(() -> postgresItemRepository.findById(id));
    }

    @Async
    public CompletableFuture<ItemResponse> updateItem(Long id, ItemRequest request) {
        if (!featureFlagProperties.isFeatureFlag()) {
            return CompletableFuture.supplyAsync(() -> itemRepository.update(id, request.name(), request.description()));
        }
        return CompletableFuture.supplyAsync(() -> postgresItemRepository.update(id, request.name(), request.description()));
    }

    @Async
    public CompletableFuture<Boolean> deleteItem(Long id) {
        if (!featureFlagProperties.isFeatureFlag()) {
            return CompletableFuture.supplyAsync(() -> itemRepository.delete(id));
        }
        return CompletableFuture.supplyAsync(() -> postgresItemRepository.delete(id));
    }
}
