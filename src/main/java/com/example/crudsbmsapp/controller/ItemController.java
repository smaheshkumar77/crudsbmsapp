package com.example.crudsbmsapp.controller;

import com.example.crudsbmsapp.dto.ItemRequest;
import com.example.crudsbmsapp.dto.ItemResponse;
import com.example.crudsbmsapp.service.ItemService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping
    public CompletableFuture<ResponseEntity<ItemResponse>> create(@Valid @RequestBody ItemRequest request) {
        return itemService.createItem(request)
                .thenApply(ResponseEntity::ok);
    }

    @GetMapping("/{id}")
    public CompletableFuture<ResponseEntity<ItemResponse>> getById(@PathVariable Long id) {
        return itemService.getItem(id)
                .thenApply(item -> item.map(ResponseEntity::ok)
                        .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build()));
    }

    @PutMapping("/{id}")
    public CompletableFuture<ResponseEntity<ItemResponse>> update(@PathVariable Long id, @Valid @RequestBody ItemRequest request) {
        return itemService.updateItem(id, request)
                .thenApply(ResponseEntity::ok);
    }

    @DeleteMapping("/{id}")
    public CompletableFuture<ResponseEntity<Void>> delete(@PathVariable Long id) {
        return itemService.deleteItem(id)
                .thenApply(deleted -> deleted ? ResponseEntity.noContent().build() : ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
}
