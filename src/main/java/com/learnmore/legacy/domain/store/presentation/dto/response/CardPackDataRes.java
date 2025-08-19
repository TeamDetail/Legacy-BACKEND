package com.learnmore.legacy.domain.store.presentation.dto.response;

import com.learnmore.legacy.domain.store.model.Store;
import com.learnmore.legacy.domain.store.model.enums.StoreType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CardPackDataRes {
    private String cardpackName;
    private String cardpackContent;
    private Integer price;
    private StoreType storeType;
    private Long cardpackId;

    public static CardPackDataRes from(Store store) {
        return CardPackDataRes.builder()
                .cardpackName(store.getStoreName())
                .cardpackContent(store.getStoreContent())
                .price(store.getPrice())
                .storeType(store.getStoreType())
                .cardpackId(store.getStoreId())
                .build();
    }
}
