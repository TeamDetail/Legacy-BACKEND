package com.learnmore.legacy.domain.mail.presentation.dto.response;

import com.learnmore.legacy.domain.mail.model.Mail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MailRes {
    private String mailTitle;
    private String mailContent;
    private String sendAt;
    private List<ItemData> itemData;

    public static MailRes from(Mail mail, List<Mail> item) {
        List<ItemData> items = item.stream()
                .map(ItemData::from)
                .collect(Collectors.toList());

        return MailRes.builder()
                .mailTitle(mail.getMailTitle())
                .mailContent(mail.getMailContent())
                .sendAt(mail.getSendAt())
                .itemData(items)
                .build();
    }
}
