package com.ruoyi.system.service;
import java.util.*;

public interface IAiAssistantService {
    String getAiReply(String input);
    String callAI(String systemPrompt, String userInput);
    List<Map<String, String>> getChatHistory(Long userId);
    void saveChatHistory(Long userId, String messages);
    void clearChatHistory(Long userId);
}
