package com.learnmore.legacy.domain.user.service.util;

import com.learnmore.legacy.domain.user.model.User;

public class UserUtil {

    // 레벨업 계산
    public static int levelUp(User user, int gainedExp) {
        int level = 0;
        int exp = user.getExp() + gainedExp;

        while (true) {
            int need = needExpForLevel(level);
            if (exp >= need) {
                exp -= need;
                level += 1;
            } else {
                break;
            }
        }

        user.updateLevel(level);
        user.updateExp(exp);
        return level;
    }

    // 레벨별 다음 레벨까지 필요한 경험치 계산
    private static int needExpForLevel(int level) {
        int baseExp = 50;
        int factor = 1 << ((level - 1) / 20);
        return baseExp * factor;
    }
}
