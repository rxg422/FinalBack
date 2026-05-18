package com.jbro.chat.model.dao;

import com.jbro.chat.model.vo.ChatVO;
import java.util.List;

public interface ChatDAO {
    int insertChat(ChatVO chat);
    List<ChatVO> selectAllHistory();
}