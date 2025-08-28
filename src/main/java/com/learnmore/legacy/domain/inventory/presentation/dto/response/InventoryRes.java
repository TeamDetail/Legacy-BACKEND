package com.learnmore.legacy.domain.inventory.presentation.dto.response;

import com.learnmore.legacy.domain.inventory.model.InventoryHistory;
import com.learnmore.legacy.domain.inventory.model.enums.ItemType;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InventoryRes {
    private Long itemId;
    private ItemType itemType;
    private String itemName;
    private String itemDescription;
    private int itemCount;

    public static InventoryRes from(InventoryHistory history) {
        return InventoryRes.builder()
                .itemId(history.getInventory().getItemId())
                .itemType(history.getInventory().getItemType())
                .itemName(history.getInventory().getItemName())
                .itemDescription(history.getInventory().getItemDescription())
                .itemCount(history.getItemCount())
                .build();
    }
}
