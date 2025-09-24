package com.learnmore.legacy.domain.mail.presentation.dto.response;

import com.learnmore.legacy.domain.mail.model.Mail;
import com.learnmore.legacy.domain.store.model.enums.StoreType;
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
    private StoreType itemType;
    private String itemName;
    private String itemDescription;
    private Integer itemCount;

    public static ItemData from(Mail mail) {
        // 카드팩 마다 아이디 지정하기
        Long itemId = mail.getItemType() == StoreType.CARD_PACK ? 1L : 2L;

        return ItemData.builder()
                .itemId(itemId)
                .itemType(mail.getItemType())
                .itemName(mail.getItemName())
                .itemDescription(mail.getItemDescription())
                .itemCount(mail.getAwardCount())
                .build();
    }
}
