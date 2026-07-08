package com.example.crudsbmsapp.dto;

import java.time.Instant;

public record ItemResponse(Long id, String name, String description, Instant createdAt) {
}
