package com.learnmore.legacy.domain.card.presentation.dto.response;

import com.learnmore.legacy.domain.card.model.NationAttribute;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NationAttributeRes {
    private Long nationAttributeId;
    private String attributeName;

    public static NationAttributeRes from(NationAttribute nationAttribute) {
        return NationAttributeRes.builder()
                .nationAttributeId(nationAttribute.getNationAttributeId())
                .attributeName(nationAttribute.getAttributeName())
                .build();
    }
}
