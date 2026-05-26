package com.jbro.chat.service;

import com.jbro.chat.model.dao.ChatDAO;
import com.jbro.chat.model.vo.ChatVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatHistoryServiceImpl implements ChatHistoryService {

    private final ChatDAO chatDAO;

    @Override
    public void saveChat(ChatVO vo) {
        chatDAO.insertChat(vo);
    }

    @Override
    public List<ChatVO> getAllHistory(int userNo) {
        return chatDAO.selectAllHistory(userNo);
    }
}