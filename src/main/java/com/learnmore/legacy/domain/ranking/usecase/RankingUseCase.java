package com.learnmore.legacy.domain.ranking.usecase;

import com.learnmore.legacy.domain.friends.service.FriendService;
import com.learnmore.legacy.domain.ranking.model.enums.RankingType;
import com.learnmore.legacy.domain.ranking.presentation.dto.response.BlockRankingRes;
import com.learnmore.legacy.domain.ranking.presentation.dto.response.LevelRankingRes;
import com.learnmore.legacy.domain.ranking.presentation.dto.response.UserStyleRes;
import com.learnmore.legacy.domain.user.model.Style;
import com.learnmore.legacy.domain.user.model.User;
import com.learnmore.legacy.domain.user.service.StyleService;
import com.learnmore.legacy.domain.user.service.UserService;
import com.learnmore.legacy.global.common.repo.UserSessionHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class RankingUseCase {
    private final UserService userService;
    private final StyleService styleService;
    private final FriendService friendService;
    private final UserSessionHolder userSessionHolder;


    public List<BlockRankingRes> getTopUserRanking(RankingType type) {
        List<User> topUsers;
        Long userId = userSessionHolder.get().getUserId();

        if(type == RankingType.ALL) {
            // 전체 유저 랭킹
            topUsers = userService.blockRanking();
        } else {
            // 친구 유저 랭킹 (친구 ID만 먼저 조회 후 해당 유저들만 조회)
            topUsers = friendService.blockRanking(userId);
        }

        List<Style> equippedStyles = styleService.findAllEquippedStyles(topUsers);

        Map<Long, Style> userIdToStyle = equippedStyles.stream()
                .collect(Collectors.toMap(
                        s -> s.getUser().getUserId(),
                        Function.identity()
                ));

        return topUsers.stream()
                .map(user -> {
                    Style style = userIdToStyle.get(user.getUserId());
                    UserStyleRes styleDto = UserStyleRes.from(style);

                    return new BlockRankingRes(
                            user.getNickname(),
                            user.getLevel(),
                            user.getAllBlocks(),
                            user.getImageUrl(),
                            styleDto
                    );
                })
                .collect(Collectors.toList());
    }

    public List<LevelRankingRes> getTopUserLevelRanking(RankingType type) {
        List<User> topUsers;
        Long userId = userSessionHolder.get().getUserId();

        // 타입 별로 조회
        if(type == RankingType.ALL) {
            topUsers = userService.levelRanking();
        }else {
            topUsers = friendService.levelRanking(userId);
        }
        List<Style> equippedStyles =  styleService.findAllEquippedStyles(topUsers);

        Map<Long, Style> userIdToStyle = equippedStyles.stream()
                .collect(Collectors.toMap(
                        s -> s.getUser().getUserId(),
                        Function.identity()
                ));

        return topUsers.stream()
                .map(user -> {
                    Style style = userIdToStyle.get(user.getUserId());
                    UserStyleRes styleDto = UserStyleRes.from(style);

                    return new LevelRankingRes(
                            user.getNickname(),
                            user.getLevel(),
                            user.getExp(),
                            user.getImageUrl(),
                            styleDto
                    );
                })
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Integer getExploreRanking(Long userId) {
        List<User> topUsers = userService.blockRanking();

        return IntStream.range(0, topUsers.size())
                .filter(i -> topUsers.get(i).getUserId().equals(userId))
                .map(i -> i + 1)
                .findFirst()
                .orElse(-1);
    }


    @Transactional(readOnly = true)
    public Integer getLevelRanking(Long userId) {
        List<User> topUsers = userService.levelRanking();

        return IntStream.range(0, topUsers.size())
                .filter(i -> topUsers.get(i).getUserId().equals(userId))
                .map(i -> i + 1)
                .findFirst()
                .orElse(-1);
    }

}
