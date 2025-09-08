package com.learnmore.legacy.domain.mail.service;

import com.learnmore.legacy.domain.inventory.model.Inventory;
import com.learnmore.legacy.domain.inventory.model.InventoryHistory;
import com.learnmore.legacy.domain.inventory.model.repo.InventoryHistoryJpaRepo;
import com.learnmore.legacy.domain.inventory.model.repo.InventoryJpaRepo;
import com.learnmore.legacy.domain.mail.model.Mail;
import com.learnmore.legacy.domain.mail.model.repo.MailJpaRepo;
import com.learnmore.legacy.domain.mail.presentation.dto.response.MailRes;
import com.learnmore.legacy.domain.user.model.User;
import com.learnmore.legacy.domain.user.model.repo.UserJpaRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MailService {
    private final MailJpaRepo mailJpaRepo;
    private final UserJpaRepo userJpaRepo;
    private final InventoryJpaRepo inventoryJpaRepo;
    private final InventoryHistoryJpaRepo inventoryHistoryJpaRepo;

    public List<MailRes> getAllMyMails(Long userId) {
        List<Mail> mails = mailJpaRepo.findAllByUser_UserId(userId);

        // mailTitle 기준으로 그룹핑
        Map<String, List<Mail>> mailGroup = mails.stream()
                .collect(Collectors.groupingBy(Mail::getMailTitle));

        // 그룹핑된 데이터 → MailRes 변환
        return mailGroup.values().stream()
                .map(mailList -> MailRes.from(mailList.get(0), mailList))
                .collect(Collectors.toList());
    }

    @Transactional
    public List<MailRes> getAllMails(Long userId) {
        User user = userJpaRepo.findByUserId(userId);

        List<Mail> mails = mailJpaRepo.findAllByUser_UserId(userId);
        if (mails.isEmpty()) {
            return Collections.emptyList();
        }

        List<MailRes> results = new ArrayList<>();

        for (Mail mail : mails) {
            Optional<Inventory> optionalInventory =
                    inventoryJpaRepo.findByItemTypeAndItemName(mail.getItemType(), mail.getItemName());

            Inventory inventory;
            if (optionalInventory.isPresent()) {
                inventory = optionalInventory.get();
            } else {
                inventory = Inventory.builder()
                        .itemId(mail.getMailId())
                        .itemType(mail.getItemType())
                        .itemName(mail.getItemName())
                        .itemDescription(mail.getItemDescription())
                        .build();
                inventoryJpaRepo.save(inventory);
            }

            InventoryHistory history = InventoryHistory.builder()
                    .user(user)
                    .inventory(inventory)
                    .store(mail.getStore())
                    .itemCount(mail.getAwardCount())
                    .build();
            inventoryHistoryJpaRepo.save(history);

            results.add(MailRes.from(mail, Collections.singletonList(mail)));
        }

        mailJpaRepo.deleteAll(mails);

        return results;
    }

}
