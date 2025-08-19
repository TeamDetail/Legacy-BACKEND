package com.learnmore.legacy.domain.inventory.presentation.dto.response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.learnmore.legacy.domain.inventory.model.InventoryHistory;
import com.learnmore.legacy.domain.inventory.model.enums.ItemType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InventoryItemRes {
    private ItemType itemType;
    private ItemDataRes itemData;
    private int itemCount;

    public static InventoryItemRes from(InventoryHistory history) {
        ObjectMapper mapper = new ObjectMapper();
        ItemDataRes itemDataRes;
        try {
            itemDataRes = mapper.readValue(history.getInventory().getItemData(), ItemDataRes.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("아이템 데이터 파싱 실패", e);
        }

        return InventoryItemRes.builder()
                .itemType(history.getInventory().getItemType())
                .itemData(itemDataRes)
                .itemCount(history.getItemCount())
                .build();
    }
}
