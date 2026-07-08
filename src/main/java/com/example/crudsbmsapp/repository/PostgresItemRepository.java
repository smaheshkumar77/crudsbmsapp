package com.example.crudsbmsapp.repository;

import com.example.crudsbmsapp.dto.ItemResponse;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Optional;

@Repository
public class PostgresItemRepository {
    private final JdbcTemplate jdbcTemplate;

    public PostgresItemRepository(DataSource postgresDataSource) {
        this.jdbcTemplate = new JdbcTemplate(postgresDataSource);
    }

    public ItemResponse create(String name, String description) {
        String sql = "INSERT INTO items (name, description, created_at) VALUES (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        Instant createdAt = Instant.now();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, name);
            ps.setString(2, description);
            ps.setTimestamp(3, Timestamp.from(createdAt));
            return ps;
        }, keyHolder);

        Long id = extractGeneratedId(keyHolder);
        return new ItemResponse(id, name, description, createdAt);
    }

    private Long extractGeneratedId(KeyHolder keyHolder) {
        if (keyHolder.getKeys() != null && !keyHolder.getKeys().isEmpty()) {
            Object idValue = keyHolder.getKeys().get("id");
            if (idValue instanceof Number number) {
                return number.longValue();
            }
        }
        return keyHolder.getKey() != null ? keyHolder.getKey().longValue() : 1L;
    }

    public Optional<ItemResponse> findById(Long id) {
        return jdbcTemplate.query(
                "SELECT id, name, description, created_at FROM items WHERE id = ?",
                rs -> rs.next() ? Optional.of(new ItemResponse(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getTimestamp("created_at").toInstant()
                )) : Optional.empty(),
                id
        );
    }

    public ItemResponse update(Long id, String name, String description) {
        jdbcTemplate.update("UPDATE items SET name = ?, description = ? WHERE id = ?", name, description, id);
        return findById(id).orElseThrow(() -> new IllegalArgumentException("Item not found: " + id));
    }

    public boolean delete(Long id) {
        int updated = jdbcTemplate.update("DELETE FROM items WHERE id = ?", id);
        return updated > 0;
    }
}
