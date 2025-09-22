package com.learnmore.legacy.domain.inventory.model.repo;

import com.learnmore.legacy.domain.inventory.model.InventoryHistory;
import com.learnmore.legacy.domain.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventoryHistoryJpaRepo extends JpaRepository<InventoryHistory, Long> {
    List<InventoryHistory> findAllByUser_UserId(Long userId);

    InventoryHistory findByStore_StoreId(Long cardpackId);

    InventoryHistory findByInventory_InventoryIdAndUser(Long inventoryId, User user);
}
