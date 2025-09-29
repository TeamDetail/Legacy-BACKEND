package com.learnmore.legacy.domain.friends.service.util;

import java.util.HashMap;
import java.util.Map;

public class FriendCodeUtil {

    private static final char[] BASE62_CHARS =
            "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".toCharArray();
    private static final int CODE_LENGTH = 6;

    // 디코딩 성능 향상을 위한 룩업 맵
    private static final Map<Character, Integer> CHAR_TO_VALUE = new HashMap<>();
    static {
        for (int i = 0; i < BASE62_CHARS.length; i++) {
            CHAR_TO_VALUE.put(BASE62_CHARS[i], i);
        }
    }

    public static String encode(long value) {
        StringBuilder sb = new StringBuilder();
        do {
            int i = (int)(value % 62);
            sb.append(BASE62_CHARS[i]);
            value /= 62;
        } while (value > 0);

        while (sb.length() < CODE_LENGTH) {
            sb.append('0');
        }

        return sb.reverse().toString();
    }

    public static long decode(String code) {
        if (code == null || code.isEmpty()) {
            throw new IllegalArgumentException("친구 코드가 비어있습니다.");
        }

        // 공백 제거
        code = code.trim();

        if (code.length() != CODE_LENGTH) {
            throw new IllegalArgumentException(
                    String.format("친구 코드는 %d자리여야 합니다. 입력된 코드: %s (%d자리)",
                            CODE_LENGTH, code, code.length())
            );
        }

        long result = 0;
        long power = 1;

        for (int i = code.length() - 1; i >= 0; i--) {
            char c = code.charAt(i);
            Integer digit = CHAR_TO_VALUE.get(c);

            if (digit == null) {
                throw new IllegalArgumentException(
                        String.format("유효하지 않은 문자 '%c'가 코드에 포함되어 있습니다. 위치: %d", c, i)
                );
            }

            result += digit * power;
            power *= 62;
        }

        return result;
    }
}

