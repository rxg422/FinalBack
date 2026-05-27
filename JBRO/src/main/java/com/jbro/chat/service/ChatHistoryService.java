package com.jbro.chat.service;

import com.jbro.chat.model.vo.ChatVO;
import java.util.List;

public interface ChatHistoryService {
    void saveChat(ChatVO vo); 
    List<ChatVO> getAllHistory(int userNo); 
}