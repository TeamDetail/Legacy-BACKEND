package com.learnmore.legacy.domain.inventory.model.repo;

import com.learnmore.legacy.domain.inventory.model.Inventory;
import com.learnmore.legacy.domain.inventory.model.enums.ItemType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface InventoryJpaRepo extends JpaRepository<Inventory, Long> {
    Optional<Inventory> findByItemTypeAndItemData(ItemType itemType, String itemData);
}
