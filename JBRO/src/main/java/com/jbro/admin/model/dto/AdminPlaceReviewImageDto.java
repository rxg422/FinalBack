package com.jbro.admin.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AdminPlaceReviewImageDto {
    private Long reviewId;
    private String reviewImage;
    private String writerNickname;
    private String content;
}