package com.learnmore.legacy.domain.inventory.presentation.dto.response;

import com.learnmore.legacy.domain.inventory.model.InventoryHistory;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class InventoryRes {
    private InventoryItemRes item;

    public static InventoryRes from(InventoryHistory history) {
        return InventoryRes.builder()
                .item(InventoryItemRes.from(history))
                .build();
    }
}
