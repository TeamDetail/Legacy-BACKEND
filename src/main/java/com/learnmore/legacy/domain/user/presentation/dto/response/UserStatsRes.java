package com.learnmore.legacy.domain.user.presentation.dto.response;

import com.learnmore.legacy.domain.user.model.User;

public record UserStatsRes(
        Integer snowflakeCapacity,
        Integer storeRestock,
        Integer creditCollect,
        Integer dropCount
) {
    public static UserStatsRes from(User user) {
        return new UserStatsRes(
                user.getSnowflakeCapacity(),
                user.getStoreRestock(),
                user.getCreditCollect(),
                user.getDropCount()
        );
    }
}
