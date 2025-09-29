package com.learnmore.legacy.domain.freinds.service.util;

/**
 * 사용자의 고유 ID를 6자리의 친구 코드로 변환하는 유틸리티 클래스
 * Base62 인코딩을 사용하여 숫자, 대소문자 알파벳으로 구성된 코드를 생성합니다.
 */
public class FriendCodeUtil {

    private static final char[] BASE62_CHARS =
            "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".toCharArray();
    private static final int CODE_LENGTH = 6;

    /**
     * 사용자 ID(long)를 6자리 친구 코드로 인코딩합니다.
     * @param value 사용자 ID
     * @return 6자리 친구 코드
     */
    public static String encode(long value) {
        StringBuilder sb = new StringBuilder();
        do {
            int i = (int)(value % 62);
            sb.append(BASE62_CHARS[i]);
            value /= 62;
        } while (value > 0);

        // 코드가 6자리 미만일 경우, 앞을 '0'으로 채웁니다.
        while (sb.length() < CODE_LENGTH) {
            sb.append('0');
        }

        // Base62 변환 결과는 역순이므로 뒤집어서 반환합니다.
        return sb.reverse().toString();
    }

    /**
     * 6자리 친구 코드를 사용자 ID(long)로 디코딩합니다.
     * @param code 6자리 친구 코드
     * @return 사용자 ID
     */
    public static long decode(String code) {
        if (code == null || code.length() != CODE_LENGTH) {
            throw new IllegalArgumentException("유효하지 않은 친구 코드입니다.");
        }
        long result = 0;
        long power = 1;
        for (int i = code.length() - 1; i >= 0; i--) {
            int digit = new String(BASE62_CHARS).indexOf(code.charAt(i));
            if (digit == -1) {
                throw new IllegalArgumentException("유효하지 않은 문자가 코드에 포함되어 있습니다.");
            }
            result += digit * power;
            power *= 62;
        }
        return result;
    }
}

