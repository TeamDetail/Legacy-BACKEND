package com.learnmore.legacy.domain.user.presentation.dto.response;

import com.learnmore.legacy.domain.user.model.User;

public record UserAdventureRes(
		Integer rank,
		Integer allBlocks,
		Integer ruinsBlocks,
		Integer solvedQuizs,
		Integer wrongQuizes,
		Integer clearCourse,
		Integer makeCourse,
		long commentCount
) {
	public static UserAdventureRes from(
			User user,
			Integer rank,
			Integer solvedQuizs,
			Integer wrongQuizes,
			Integer clearCourse,
			Integer makeCourse,
			long commentCount) {
		return new UserAdventureRes(
				rank,
				user.getAllBlocks(),
				user.getRuinsBlocks(),
				solvedQuizs,
				wrongQuizes,
				clearCourse,
				makeCourse,
				commentCount
		);
	}
}
