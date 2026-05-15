package com.jbro.chat.service;

import com.jbro.chat.dto.ChatRequest;
import com.jbro.chat.entity.ChatHistory;
import com.jbro.chat.repository.ChatHistoryRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatHistoryServiceImpl implements ChatHistoryService {

    private final ChatHistoryRepository chatHistoryRepository;

    @Override
    @Transactional
    public void saveChat(ChatRequest dto) {
        ChatHistory history = new ChatHistory();
        history.setQuestion(dto.getQuestion());
        history.setAnswer(dto.getAnswer());
        chatHistoryRepository.save(history);
    }

    @Override
    public List<ChatHistory> getAllHistory() {
        return chatHistoryRepository.findAllByOrderByRegDateAsc();
    }
}