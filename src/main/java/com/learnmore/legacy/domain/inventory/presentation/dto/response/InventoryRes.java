package com.learnmore.legacy.domain.inventory.presentation.dto.response;

import com.learnmore.legacy.domain.inventory.model.InventoryHistory;
import com.learnmore.legacy.domain.store.model.enums.StoreType;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InventoryRes {
    private Long itemId;
    private StoreType itemType;
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
