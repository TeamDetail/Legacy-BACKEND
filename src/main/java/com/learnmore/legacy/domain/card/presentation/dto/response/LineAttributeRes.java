package com.learnmore.legacy.domain.card.presentation.dto.response;

import com.learnmore.legacy.domain.card.model.LineAttribute;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LineAttributeRes {
    private Long lineAttributeId;
    private String attributeName;

    public static LineAttributeRes from(LineAttribute line) {
        return LineAttributeRes.builder()
                .lineAttributeId(line.getLineAttributeId())
                .attributeName(line.getAttributeName())
                .build();
    }
}
