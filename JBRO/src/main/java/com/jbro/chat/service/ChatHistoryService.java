package com.jbro.chat.service;

import java.util.List;

import com.jbro.chat.dto.ChatRequest;
import com.jbro.chat.entity.ChatHistory;

public interface ChatHistoryService {
	void saveChat(ChatRequest dto);
	List<ChatHistory> getAllHistory();
}
