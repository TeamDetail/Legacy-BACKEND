package com.learnmore.legacy.domain.friends.service.util;

import java.util.HashMap;
import java.util.Map;

public class FriendCodeUtil {

    private static final char[] BASE62_CHARS =
            "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".toCharArray();
    private static final int CODE_LENGTH = 12;

    // 난독화(Obfuscation)를 위한 XOR 키.
    // 실제 userId와 encodedValue 간의 연관성을 끊어 유추를 어렵게 합니다.
    // 6자리 Base62 코드가 표현할 수 있는 최대값(약 560억)보다 작습니다.
    private static final long XOR_KEY = 7179868779L;

    // 디코딩 성능 향상을 위한 룩업 맵
    private static final Map<Character, Integer> CHAR_TO_VALUE = new HashMap<>();
    static {
        for (int i = 0; i < BASE62_CHARS.length; i++) {
            CHAR_TO_VALUE.put(BASE62_CHARS[i], i);
        }
    }

    /**
     * User ID를 난독화하여 12자리 고정 길이의 Base62 친구 코드로 인코딩합니다.
     * @param userId 인코딩할 유저 ID
     * @return 12자리 Base62 친구 코드
     */
    public static String encode(long userId) {
        // 1. 난독화: 유저 ID와 XOR 연산을 수행하여 원본 ID 유추를 어렵게 합니다.
        long obfuscatedValue = userId ^ XOR_KEY;

        // 2. Base62 변환
        StringBuilder sb = new StringBuilder();
        long value = obfuscatedValue;

        if (value == 0) {
            sb.append(BASE62_CHARS[0]);
        } else {
            do {
                int i = (int)(value % 62);
                sb.append(BASE62_CHARS[i]);
                value /= 62;
            } while (value > 0);
        }

        // 3. 12자리 고정 길이를 위해 남은 부분을 '0'으로 패딩 (가장 앞쪽에 0이 채워짐)
        while (sb.length() < CODE_LENGTH) {
            sb.append('0');
        }

        return sb.reverse().toString();
    }

    /**
     * 12자리 고정 길이 Base62 친구 코드를 디코딩하여 원본 User ID를 반환합니다.
     * @param code 12자리 Base62 친구 코드
     * @return 원본 User ID
     */
    public static long decode(String code) {
        if (code == null || code.isEmpty()) {
            throw new IllegalArgumentException("친구 코드가 비어있습니다.");
        }

        // 공백 제거 및 길이 확인
        code = code.trim();

        if (code.length() != CODE_LENGTH) {
            throw new IllegalArgumentException(
                    String.format("친구 코드는 %d자리여야 합니다. 입력된 코드: %s (%d자리)",
                            CODE_LENGTH, code, code.length())
            );
        }

        // 1. Base62 디코딩
        long obfuscatedValue = 0;
        long power = 1; // 62^0, 62^1, 62^2, ...

        for (int i = code.length() - 1; i >= 0; i--) {
            char c = code.charAt(i);
            Integer digit = CHAR_TO_VALUE.get(c);

            if (digit == null) {
                throw new IllegalArgumentException(
                        String.format("유효하지 않은 문자 '%c'가 코드에 포함되어 있습니다. 위치: %d", c, i)
                );
            }

            obfuscatedValue += digit * power;
            power *= 62;
        }

        // 2. 난독화 해제: XOR 연산으로 원본 User ID 복원
        long userId = obfuscatedValue ^ XOR_KEY;

        return userId;
    }
}