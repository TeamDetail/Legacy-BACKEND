package com.learnmore.legacy.domain.fcm.presentation.dto.request;

import java.math.BigDecimal;

public record MessageReq (BigDecimal  lat,
                          BigDecimal lng,
                          String title,
                          String targetToken) {
}