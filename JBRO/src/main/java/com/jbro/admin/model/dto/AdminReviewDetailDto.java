package com.jbro.admin.model.dto;

import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AdminReviewDetailDto {
    private Long reviewId;
    private Long contentId;
    private String writerNickname;
    private String placeTitle;
    private String content;
    private String reviewImage;
    private String reviewActive;
    private LocalDateTime createdAt;
}