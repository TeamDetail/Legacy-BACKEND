package com.learnmore.legacy.domain.fcm.usecase;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.learnmore.legacy.domain.fcm.presentation.dto.request.MessageReq;
import com.learnmore.legacy.domain.ruins.model.Ruins;
import com.learnmore.legacy.domain.ruins.service.RuinsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FcmUseCase {

    private final RuinsService ruinsService;

    public void ruinsAlarm (MessageReq req) {
        Ruins nearestRuins =ruinsService.findNearestRuins(req.lat(),req.lng());
        try {
            FirebaseMessaging.getInstance().send(Message.builder()
                    .setNotification(Notification.builder()
                            .setTitle(req.title())
                            .setBody(nearestRuins.getName())
                            .build())
                    .setToken(req.targetToken())
                    .build());
        } catch (FirebaseMessagingException e) {
            System.out.println("FCM 전송 실패: " + e.getMessage());
        }
    }

}