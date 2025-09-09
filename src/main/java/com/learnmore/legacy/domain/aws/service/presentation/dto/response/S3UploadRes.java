package com.learnmore.legacy.domain.aws.service.presentation.dto.response;

public record S3UploadRes(
        String uploadUrl,
        String imageUrl
) {
}
