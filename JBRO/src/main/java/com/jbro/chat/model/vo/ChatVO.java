package com.jbro.chat.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatVO {
    private Long id;
    private Long userId;    // DB의 USER_ID와 매핑
    private String question;
    private String answer;
    private String regDate;
}