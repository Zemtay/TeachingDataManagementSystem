import request from '@/utils/request'

// AI助手对话接口
export function getAiAnswer(input) {
  return request({
    url: '/dashboard/ai-assistant/answer',
    method: 'POST',
    data: JSON.stringify({ input: input }) // `input=${encodeURIComponent(input)}`
  })
}

// 获取聊天记录
export function getChatHistory() {
  return request({
    url: '/dashboard/ai-assistant/chatHistory',
    method: 'get'
  })
}

// 保存聊天记录
export function saveChatHistory(messages) {
  return request({
    url: '/dashboard/ai-assistant/chatHistory',
    method: 'post',
    data: { messages }
  })
}

export function clearChatHistory() {
  return request({
    url: '/dashboard/ai-assistant/chatHistory',
    method: 'delete'
  })
}
