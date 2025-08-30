package com.learnmore.legacy.domain.mail.presentation.dto.response;

import com.learnmore.legacy.domain.inventory.model.enums.ItemType;
import com.learnmore.legacy.domain.mail.model.Mail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemData {
    private Long itemId;
    private ItemType itemType;
    private String itemName;
    private String itemDescription;
    private Integer itemCount;

    public static ItemData from(Mail mail) {
        Long itemId = mail.getItemType() == ItemType.CARD_PACK ? 1L : 2L;

        return ItemData.builder()
                .itemId(itemId)
                .itemType(mail.getItemType())
                .itemName(mail.getItemName())
                .itemDescription(mail.getItemDescription())
                .itemCount(mail.getAwardCount())
                .build();
    }
}
