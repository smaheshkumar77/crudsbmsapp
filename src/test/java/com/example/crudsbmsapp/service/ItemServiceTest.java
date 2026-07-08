package com.example.crudsbmsapp.service;

import com.example.crudsbmsapp.dto.ItemRequest;
import com.example.crudsbmsapp.repository.ItemRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.test.context.TestPropertySource;

import javax.sql.DataSource;
import java.util.concurrent.ExecutionException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@TestPropertySource(properties = "app.feature-flag=false")
class ItemServiceTest {

    @Autowired
    private ItemService itemService;

    @Autowired
    private ItemRepository itemRepository;

    @Test
    void createItemShouldPersistInBothDatabasesWhenFeatureFlagIsOff() throws ExecutionException, InterruptedException {
        ItemRequest request = new ItemRequest("Alpha", "Initial item");

        var created = itemService.createItem(request).get();

        assertThat(created.name()).isEqualTo("Alpha");
        assertThat(itemService.getItem(created.id()).get()).isPresent();
    }

    @Test
    void updateItemShouldReturnUpdatedRecord() throws ExecutionException, InterruptedException {
        var created = itemService.createItem(new ItemRequest("Beta", "Original desc")).get();

        var updated = itemService.updateItem(created.id(), new ItemRequest("Beta", "Updated desc")).get();

        assertThat(updated.description()).isEqualTo("Updated desc");
    }

    @Test
    void createItemShouldRollbackH2WhenPostgresWriteFails() throws ExecutionException, InterruptedException {
        DataSource dataSource = new DriverManagerDataSource();
        dataSource = new DriverManagerDataSource();
        ((DriverManagerDataSource) dataSource).setDriverClassName("org.h2.Driver");
        ((DriverManagerDataSource) dataSource).setUrl("jdbc:h2:mem:rollbackdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE");
        ((DriverManagerDataSource) dataSource).setUsername("sa");
        ((DriverManagerDataSource) dataSource).setPassword("");

        ItemService rollbackService = new ItemService(
                itemRepository,
                new com.example.crudsbmsapp.repository.PostgresItemRepository(dataSource) {
                    @Override
                    public com.example.crudsbmsapp.dto.ItemResponse create(String name, String description) {
                        throw new RuntimeException("postgres unavailable");
                    }
                },
                new com.example.crudsbmsapp.config.FeatureFlagProperties()
        );

        assertThatThrownBy(() -> rollbackService.createItem(new ItemRequest("Gamma", "Rollback item")).get())
                .isInstanceOf(ExecutionException.class)
                .hasRootCauseInstanceOf(RuntimeException.class);

        assertThat(itemRepository.existsByName("Gamma")).isFalse();
    }
}
