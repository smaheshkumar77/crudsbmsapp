package com.example.crudsbmsapp.mcp;

import com.example.crudsbmsapp.dto.ItemRequest;
import com.example.crudsbmsapp.dto.ItemResponse;
import com.example.crudsbmsapp.service.ItemService;
import org.springframework.ai.mcp.annotation.McpTool;
import org.springframework.ai.mcp.annotation.McpToolParam;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ItemMcpTools {

    private final ItemService itemService;

    public ItemMcpTools(ItemService itemService) {
        this.itemService = itemService;
    }

    @McpTool(name = "create_item", description = "Create a new item with a name and description. Returns the created item.")
    public ItemResponse createItem(
            @McpToolParam(description = "Name of the item", required = true) String name,
            @McpToolParam(description = "Description of the item", required = false) String description) {
        return itemService.createItem(new ItemRequest(name, description)).join();
    }

    @McpTool(name = "get_item", description = "Get an item by its numeric id. Returns the item or a helpful not-found message.")
    public Object getItem(@McpToolParam(description = "The item id", required = true) Long id) {
        Optional<ItemResponse> result = itemService.getItem(id).join();
        return result.<Object>map(item -> item).orElseGet(() -> "No item found with id " + id);
    }

    @McpTool(name = "update_item", description = "Update an existing item's name and description by id.")
    public ItemResponse updateItem(
            @McpToolParam(description = "The item id", required = true) Long id,
            @McpToolParam(description = "New name", required = true) String name,
            @McpToolParam(description = "New description", required = false) String description) {
        return itemService.updateItem(id, new ItemRequest(name, description)).join();
    }

    @McpTool(name = "delete_item", description = "Delete an item by id. Returns true if the item was deleted, false if it did not exist.")
    public boolean deleteItem(@McpToolParam(description = "The item id", required = true) Long id) {
        return itemService.deleteItem(id).join();
    }
}
