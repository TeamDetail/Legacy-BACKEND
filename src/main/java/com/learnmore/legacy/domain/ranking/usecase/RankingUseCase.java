package com.learnmore.legacy.domain.ranking.usecase;

import com.learnmore.legacy.domain.ranking.model.enums.RankingType;
import com.learnmore.legacy.domain.ranking.presentation.dto.response.BlockRankingRes;
import com.learnmore.legacy.domain.ranking.presentation.dto.response.LevelRankingRes;
import com.learnmore.legacy.domain.ranking.presentation.dto.response.UserStyleRes;
import com.learnmore.legacy.domain.user.model.Style;
import com.learnmore.legacy.domain.user.model.User;
import com.learnmore.legacy.domain.user.service.StyleService;
import com.learnmore.legacy.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RankingUseCase {
    private final UserService userService;
    private final StyleService styleService;

    public List<BlockRankingRes> getTopUserRanking() {
        List<User> topUsers = userService.blockRanking();

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
        List<User> topUsers = userService.levelRanking();
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
}
